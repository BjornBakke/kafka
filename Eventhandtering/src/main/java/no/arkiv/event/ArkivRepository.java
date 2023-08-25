package no.arkiv.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

interface ArkivRepository extends JpaRepository<DokumentEvent, Long> {
    Optional<DokumentEvent> findByDokumentid(String code);

    @Modifying
    @Query("update DokumentEvent p set p.sideantall = :sideantall where p.dokumentid = :dokumentid")
    void updateSideantall(@Param("dokumentid") String dokumentid, @Param("sideantall") BigDecimal sideantall);
}
