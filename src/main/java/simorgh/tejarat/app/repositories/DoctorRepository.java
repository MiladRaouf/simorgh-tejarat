package simorgh.tejarat.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simorgh.tejarat.app.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
