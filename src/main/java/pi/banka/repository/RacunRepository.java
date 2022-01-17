package pi.banka.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pi.banka.domain.Racun;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the RacunPrivatnihLica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RacunRepository extends JpaRepository<Racun, Long> {

    Racun findRacunByBrojRacuna(String brojRacuna);

    @Query(value = "SELECT * FROM banka.racun_privatnih_lica where broj_racuna like %?%", nativeQuery = true)
    List<Racun> findRacunBySearchTerm(String searchTerm);
}
