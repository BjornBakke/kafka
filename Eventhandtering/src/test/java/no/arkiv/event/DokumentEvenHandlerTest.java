package no.arkiv.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

import java.time.Duration;
import java.util.Optional;

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
        DokumentEvent dokumentEvent = new DokumentEvent(null, 100L, "Helsevurdering", 1);
        arkivRepository.save(dokumentEvent);
    }

    @Test
    void dokumentChangedEvent() throws JsonProcessingException {
        Document dokument = createDokument(5, 100L);
        DocumentEventRequest request = new DocumentEventRequest("DOCUMENT_CREATED", dokument, null);
        kafkaTemplate.send("arkiv-dokument-v1", request.eventType(), toString(request));
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<DokumentEvent> optionalProduct = arkivRepository.findByDokumentid("100");
                    Assertions.assertThat(optionalProduct).isPresent();
                    assertThat(optionalProduct.get().getDokumentid()).isEqualTo(100L);
                    assertThat(optionalProduct.get().getSideantall()).isEqualTo(5);
                });
    }

    private static Document createDokument(int sideantall, long dokumenthandtak) {
        Document document = new Document();
        document.numberOfPages = sideantall;
        document.documentId = dokumenthandtak;
        return document;
    }

    private String toString(DocumentEventRequest event) throws JsonProcessingException {
        return new JsonMapper().writeValueAsString(event);
    }
}
