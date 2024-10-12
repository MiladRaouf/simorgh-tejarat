package simorgh.tejarat.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointments(doctorId);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/doctor/{id}/open-appointment")
    public ResponseEntity<List<Appointment>> getOpenAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getOpenAppointments(doctorId);
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/doctor/{id}/reserved-appointment")
    public ResponseEntity<List<Appointment>> getReservedAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getReservedAppointments(doctorId);
        return ResponseEntity.ok().body(appointments);
    }

    @DeleteMapping("/delete/{id}")
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
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@RequestParam LocalDate date) {
        List<Appointment> appointments = appointmentService.getOpenAppointments(date);
        return ResponseEntity.ok().body(appointments);
    }

    @PutMapping("/reserved")
    public ResponseEntity<String> reservedByNameAndPhoneNumber(
        @RequestBody String name,
        @RequestBody String phoneNumber,
        @RequestBody Long appointmentId
    ) {
        try {
            appointmentService.reservedAppointment(name, phoneNumber, appointmentId);
            return ResponseEntity.status(HttpStatus.CREATED).body("the appointment was reserved");
        }
        catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Appointment>> getAppointmentsByPhoneNumber(@RequestParam String phoneNumber) {
        List<Appointment> appointments = appointmentService.getReservedAppointments(phoneNumber);
        return ResponseEntity.ok().body(appointments);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAppointment(
        @RequestBody Long doctorId,
        @RequestBody LocalDateTime startTime,
        @RequestBody LocalDateTime endTime
    ) {
        appointmentService.addAppointmentTimes(doctorId, startTime, endTime);
        return ResponseEntity.status(HttpStatus.CREATED).body("new appointments were created");
    }
}
