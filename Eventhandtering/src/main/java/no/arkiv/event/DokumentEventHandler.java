package no.arkiv.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
class DokumentEventHandler {
    private static final Logger log = LoggerFactory.getLogger(DokumentEventHandler.class);
    private final ArkivRepository arkivRepository;

    DokumentEventHandler(ArkivRepository arkivRepository) {
        this.arkivRepository = arkivRepository;
    }

    @KafkaListener(topics = "arkiv-dokument-v1", groupId = "pip-arkiv")
    public void handle(DokumentEventDto event) {
        log.info("Received a ProductPriceChangedEvent with productCode:{}: ", event.dokumentid());
        arkivRepository.updateSideantall(event.dokumentid(), event.sideantall());
    }
}
