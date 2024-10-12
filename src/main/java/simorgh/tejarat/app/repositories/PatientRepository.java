package simorgh.tejarat.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simorgh.tejarat.app.entities.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    public Patient findByPhoneNumber(String PhoneNumber);
}
