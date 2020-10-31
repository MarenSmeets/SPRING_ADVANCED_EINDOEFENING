package be.vdab.personeel.repositories;

import be.vdab.personeel.domain.Werknemer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {
}
