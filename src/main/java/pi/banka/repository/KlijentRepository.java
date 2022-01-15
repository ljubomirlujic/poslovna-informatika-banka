package pi.banka.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pi.banka.domain.Klijent;
import pi.banka.domain.Racun;

/**
 * Spring Data SQL repository for the Klijent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KlijentRepository extends JpaRepository<Klijent, Long> {


    @Query(value = "SELECT * FROM banka.racun_privatnih_lica where klijent_id = ?",
            nativeQuery = true)
    Klijent findByRacun(Long clinic_id);
}
