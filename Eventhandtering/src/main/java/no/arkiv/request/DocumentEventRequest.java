package no.arkiv.request;

public record DocumentEventRequest(String eventType, Document document, Document documentBeforeChange) {
}
