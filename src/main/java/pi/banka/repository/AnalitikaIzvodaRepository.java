package pi.banka.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pi.banka.domain.AnalitikaIzvoda;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the AnalitikaIzvoda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalitikaIzvodaRepository extends JpaRepository<AnalitikaIzvoda, Long> {

    @Query(value = "SELECT * FROM banka.analitika_izvoda join banka.dnevno_stanje on banka.analitika_izvoda.dnevno_stanje_id = banka.dnevno_stanje.id join banka.racun_privatnih_lica\n" +
            "on banka.dnevno_stanje.racun_privatnih_lica_id = banka.racun_privatnih_lica.id join banka.klijent on banka.racun_privatnih_lica.klijent_id = banka.klijent.id\n" +
            "where banka.klijent.id = ?1 and banka.dnevno_stanje.datum_izvoda between ?2 and ?3",nativeQuery = true)
    List<AnalitikaIzvoda> findByKlijentAndDate(Long id, LocalDate datumPocetka, LocalDate krajnjiDatum);
}
