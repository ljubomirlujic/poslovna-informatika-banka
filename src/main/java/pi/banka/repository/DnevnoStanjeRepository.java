package pi.banka.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pi.banka.domain.DnevnoStanje;

/**
 * Spring Data SQL repository for the DnevnoStanje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DnevnoStanjeRepository extends JpaRepository<DnevnoStanje, Long> {

    @Query(value = "SELECT * FROM banka.dnevno_stanje where datum_izvoda = (SELECT MAX(datum_izvoda) FROM banka.dnevno_stanje)" +
            " and racun_privatnih_lica_id = ?",
            nativeQuery = true)
    DnevnoStanje findByLastDateAndRacun(Long idRacuna);
}
