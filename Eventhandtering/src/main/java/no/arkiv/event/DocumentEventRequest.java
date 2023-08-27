package no.arkiv.event;

record DocumentEventRequest(String eventType, Document document, Document documentBeforeChange) {
}
