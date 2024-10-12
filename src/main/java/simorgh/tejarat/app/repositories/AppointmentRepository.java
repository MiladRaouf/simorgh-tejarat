package simorgh.tejarat.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simorgh.tejarat.app.entities.Appointment;
import simorgh.tejarat.app.enums.appointment.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    public List<Appointment> findByDoctorId(Long doctorId);

    public List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    public List<Appointment> findByAppointmentTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
