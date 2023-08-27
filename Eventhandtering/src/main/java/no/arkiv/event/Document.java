package no.arkiv.event;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Document {
    public long documentId;
    public String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime dateStored;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime documentDate;
    public String documentType;
    public String documentTypeGroup;
    public String status;
    public String createdBy;
    public int numberOfPages;

    public Document() {
    }
}
