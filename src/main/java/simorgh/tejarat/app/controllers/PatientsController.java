package simorgh.tejarat.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simorgh.tejarat.app.entities.Patient;
import simorgh.tejarat.app.services.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientsController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<String> add(
        @RequestBody String name,
        @RequestBody String phoneNumber
    ) {
        try {
            patientService.addPatient(name, phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body("a new patient was added");
        } catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }
}
