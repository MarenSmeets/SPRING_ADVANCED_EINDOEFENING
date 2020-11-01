package be.vdab.personeel.repositories;

import be.vdab.personeel.domain.Werknemer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {

    Optional<Werknemer> findByChefIsNull();
    Set<Werknemer> findByJobtitel_Id(long id);
}
