package no.arkiv.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import no.arkiv.db.ArkivRepository;
import no.arkiv.request.DocumentEventRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DokumentEventHandler {
    private static final Logger log = LoggerFactory.getLogger(DokumentEventHandler.class);
    private final ArkivRepository arkivRepository;

    DokumentEventHandler(ArkivRepository arkivRepository) {
        this.arkivRepository = arkivRepository;
    }

    @KafkaListener(topics = "arkiv-dokument-v1", groupId = "pip-arkiv")
    public void handle(ConsumerRecord<String, String> record) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        DocumentEventRequest request = jsonMapper.readValue(record.value(), DocumentEventRequest.class);

        log.info("MottokDocumentEventRequest på id:{}: ", request.document().documentId);
        arkivRepository.updateSideantall(request.document().documentId, request.document().numberOfPages);
        log.info("lagret på id:{}: ", request.document().documentId);
    }
}
