package simorgh.tejarat.app.services;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simorgh.tejarat.app.entities.Doctor;
import simorgh.tejarat.app.repositories.DoctorRepository;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * adds a doctor to the list of doctors
     * @param name {@link String}
     * @return {@link Doctor}
     * @throws BadRequestException
     */
    public Doctor addDoctor(String name)
    throws BadRequestException
    {
        if (name == null) throw new BadRequestException("Enter the name parameter");

        Doctor doctor = new Doctor(name);
        return doctorRepository.save(doctor);
    }
}
