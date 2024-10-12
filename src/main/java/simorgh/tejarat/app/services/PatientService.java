package simorgh.tejarat.app.services;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import simorgh.tejarat.app.entities.Patient;
import simorgh.tejarat.app.repositories.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * adds a patient to the patient list
     * @param name {@link String}
     * @param phoneNumber {@link String}
     * @return {@link Patient}
     * @throws BadRequestException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Patient registerPatient(String name, String phoneNumber)
    throws BadRequestException
    {
        if (name == null || phoneNumber == null) throw new BadRequestException("Enter the input parameters");
        // check phone number pattern

        var patient = new Patient(name, phoneNumber);
        return patientRepository.save(patient);
    }
}
