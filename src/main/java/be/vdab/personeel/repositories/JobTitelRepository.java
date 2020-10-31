package be.vdab.personeel.repositories;

import be.vdab.personeel.domain.JobTitel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitelRepository extends JpaRepository<JobTitel, Long> {
}
