package no.arkiv.db;

import jakarta.persistence.*;

@Entity
@Table(name = "dokumentevent")
public class DokumentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long dokumentid;

    @Column(nullable = false)
    private String navn;

    @Column(nullable = false)
    private int sideantall;

    public DokumentEvent() {
    }

    public DokumentEvent(Long id, Long dokumentid, String navn, int sideantall) {
        this.id = id;
        this.dokumentid = dokumentid;
        this.navn = navn;
        this.sideantall = sideantall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDokumentid() {
        return dokumentid;
    }

    public void setDokumentid(Long code) {
        this.dokumentid = code;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String name) {
        this.navn = name;
    }

    public int getSideantall() {
        return sideantall;
    }

    public void setSideantall(int price) {
        this.sideantall = price;
    }
}
