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
     *
     * @param doctorId Long
     * @return Appointment List
     */
    public List<Appointment> getReservedAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.RESERVED);
    }

    public List<Appointment> getOpenAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, AppointmentStatus.OPEN);
    }

    public List<Appointment> getAppointments(Long doctorId)
    {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public void addAppointmentTimes(Long doctorId, LocalDateTime startTime, LocalDateTime endTime)
    throws IllegalArgumentException {
        List<LocalDateTime> startAppointment = DateTimeUtility.dateTimeInterval(startTime, endTime, 30);

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("example"));

        for (LocalDateTime dateTime : startAppointment) {
            appointmentRepository.save(new Appointment(dateTime, doctor, AppointmentStatus.OPEN));
        }
    }

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

    public List<Appointment> getOpenAppointments(LocalDate date)
    {
        LocalDateTime startTime = date.atTime(0,0);
        LocalDateTime endTime = date.atTime(23,59);

        return appointmentRepository.findByAppointmentTimeBetween(startTime, endTime);
    }

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

    public List<Appointment> getReservedAppointments(String phoneNumber)
    {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber);

        return patient.getAppointments();
    }
}
