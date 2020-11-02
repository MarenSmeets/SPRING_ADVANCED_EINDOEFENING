package be.vdab.personeel.repositories;

import be.vdab.personeel.domain.Werknemer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {

    // searches DB for employee without a chef --> used in HierarchyController to initially show the highest ranking employee
    Optional<Werknemer> findByChefIsNull();
    // searches DB for employees with a specific job titel --> used in JobTitelController
    Set<Werknemer> findByJobtitel_Id(long id);
}
