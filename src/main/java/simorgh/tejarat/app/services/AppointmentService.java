package simorgh.tejarat.app.services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import simorgh.tejarat.app.entities.Appointment;
import simorgh.tejarat.app.entities.Doctor;
import simorgh.tejarat.app.entities.Patient;
import simorgh.tejarat.app.enums.appointment.AppointmentStatus;
import simorgh.tejarat.app.repositories.AppointmentRepository;
import simorgh.tejarat.app.repositories.DoctorRepository;
import simorgh.tejarat.app.repositories.PatientRepository;
import simorgh.tejarat.app.utility.DateTimeUtility;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    /**
     * gives reserved appointments of a doctor
     * @param doctorId {@link Long}
     * @return Appointment {@link List}
     */
    public List<Appointment> getReservedAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.RESERVED);
    }

    /**
     * gives open appointments of a doctor
     * @param doctorId {@link Long}
     * @return Appointment {@link List}
     */
    public List<Appointment> getOpenAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.OPEN);
    }

    /**
     * gives all appointments of a doctor
     * @param doctorId {@link Long}
     * @return Appointment {@link List}
     */
    public List<Appointment> getAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    /**
     * creates doctor's appointments within 30 minutes
     * @param doctorId {@link Long}
     * @param startTime {@link LocalDateTime}
     * @param endTime {@link LocalDateTime}
     * @throws IllegalArgumentException
     */
    public void addAppointmentTimes(Long doctorId, LocalDateTime startTime, LocalDateTime endTime)
    throws IllegalArgumentException {
        List<LocalDateTime> startAppointment = DateTimeUtility.dateTimeInterval(startTime, endTime, 30);

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("example"));

        for (LocalDateTime dateTime : startAppointment) {
            appointmentRepository.save(new Appointment(dateTime, doctor, AppointmentStatus.OPEN));
        }
    }

    /**
     * removes an open appointment and controls concurrency
     * @param appointmentId {@link Long}
     * @throws EntityNotFoundException
     * @throws AccessDeniedException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteOpenAppointment(Long appointmentId)
    throws EntityNotFoundException, AccessDeniedException
    {
        Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(
                () -> new EntityNotFoundException("Appointment with ID " + appointmentId + " not found")
            );

        if (appointment.getStatus() == AppointmentStatus.RESERVED) {
            throw new AccessDeniedException("You are not authorized to delete this appointment");
        }

        appointmentRepository.delete(appointment);
    }

    /**
     * gives one day's open appointments
     * @param date {@link LocalDate}
     * @return Appointment {@link List}
     */
    public List<Appointment> getOpenAppointments(LocalDate date)
    {
        LocalDateTime startTime = date.atTime(0,0);
        LocalDateTime endTime = date.atTime(23,59);

        return appointmentRepository.findByAppointmentTimeBetween(startTime, endTime);
    }

    /**
     * reserves a appointment and controls concurrency
     * @param name {@link String}
     * @param phoneNumber {@link String}
     * @param appointmentId {@link Long}
     * @throws EntityNotFoundException
     * @throws AccessDeniedException
     * @throws BadRequestException
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void reservedAppointment(String name, String phoneNumber, Long appointmentId)
    throws EntityNotFoundException, AccessDeniedException, BadRequestException
    {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber);

        if (patient == null) {
            PatientService patientService = new PatientService();
            patient = patientService.registerPatient(name, phoneNumber);
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
            () -> new EntityNotFoundException("Appointment with ID " + appointmentId + " not found")
        );

        if (appointment.getStatus() == AppointmentStatus.RESERVED) {
            throw new AccessDeniedException("This appointment is already booked");
        }

        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.RESERVED);

        appointmentRepository.save(appointment);
    }

    /**
     * gives the reserved appointments
     * @param phoneNumber {@link String}
     * @return Appointment {@link List}
     */
    public List<Appointment> getReservedAppointments(String phoneNumber)
    {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber);

        return patient.getAppointments();
    }
}
