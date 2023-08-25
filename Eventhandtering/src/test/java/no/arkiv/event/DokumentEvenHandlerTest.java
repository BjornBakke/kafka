package no.arkiv.event;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(properties = {"spring.kafka.consumer.auto-offset-reset=earliest", "spring.datasource.url=jdbc:tc:mysql:8.0.32:///db"})
@Testcontainers
class DokumentEvenHandlerTest {
    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.1.1"));
    @Autowired
    private ArkivRepository arkivRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        DokumentEvent dokumentEvent = new DokumentEvent(null, "100", "Helsevurdering", ONE);
        arkivRepository.save(dokumentEvent);
    }

    @Test
    void dokumentChangedEvent() {
        DokumentEventDto event = new DokumentEventDto("100", new BigDecimal("5"));
        kafkaTemplate.send("arkiv-dokument-v1", event.dokumentid(), event);
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<DokumentEvent> optionalProduct = arkivRepository.findByDokumentid("100");
                    Assertions.assertThat(optionalProduct).isPresent();
                    assertThat(optionalProduct.get().getDokumentid()).isEqualTo("100");
                    assertThat(optionalProduct.get().getSideantall()).isEqualTo(new BigDecimal("5.00"));
                });
    }
}
