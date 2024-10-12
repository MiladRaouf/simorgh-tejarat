package simorgh.tejarat.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simorgh.tejarat.app.entities.Appointment;
import simorgh.tejarat.app.services.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentsController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/")
    public ResponseEntity<Appointment> getAppointmentsByDoctorId(@RequestParam Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointments(doctorId);
        return ResponseEntity.ok((Appointment) appointments);
    }

    @GetMapping("/open-appointment")
    public ResponseEntity<Appointment> getOpenAppointmentsByDoctorId(@RequestParam Long doctorId) {
        List<Appointment> appointments = appointmentService.getOpenAppointments(doctorId);
        return ResponseEntity.ok((Appointment) appointments);
    }

    @GetMapping("/reserved-appointment")
    public ResponseEntity<Appointment> getReservedAppointmentsByDoctorId(@RequestParam Long doctorId) {
        List<Appointment> appointments = appointmentService.getReservedAppointments(doctorId);
        return ResponseEntity.ok((Appointment) appointments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            appointmentService.deleteOpenAppointment(id);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Appointment> getAppointmentsByDate(@RequestParam LocalDate date) {
        List<Appointment> appointments = appointmentService.getOpenAppointments(date);
        return ResponseEntity.ok((Appointment) appointments);
    }

    @PutMapping("/reserved")
    public ResponseEntity<Void> reservedByNameAndPhoneNumber(
        @RequestBody String name,
        @RequestBody String phoneNumber,
        @RequestBody Long appointmentId
    ) {
        try {
            appointmentService.reservedAppointment(name, phoneNumber, appointmentId);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<Appointment> getAppointmentsByPhoneNumber(@RequestParam String phoneNumber) {
        List<Appointment> appointments = appointmentService.getReservedAppointments(phoneNumber);
        return ResponseEntity.ok((Appointment) appointments);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addAppointment(
        @RequestBody Long doctorId,
        @RequestBody LocalDateTime startTime,
        @RequestBody LocalDateTime endTime
    ) {
        appointmentService.addAppointmentTimes(doctorId, startTime, endTime);
        return ResponseEntity.noContent().build();
    }
}
