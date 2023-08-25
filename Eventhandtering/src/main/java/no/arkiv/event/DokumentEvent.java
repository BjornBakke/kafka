package no.arkiv.event;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dokumentevent")
class DokumentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dokumentid;

    @Column(nullable = false)
    private String navn;

    @Column(nullable = false)
    private BigDecimal sideantall;

    public DokumentEvent() {
    }

    public DokumentEvent(Long id, String dokumentid, String navn, BigDecimal sideantall) {
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

    public String getDokumentid() {
        return dokumentid;
    }

    public void setDokumentid(String code) {
        this.dokumentid = code;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String name) {
        this.navn = name;
    }

    public BigDecimal getSideantall() {
        return sideantall;
    }

    public void setSideantall(BigDecimal price) {
        this.sideantall = price;
    }
}
