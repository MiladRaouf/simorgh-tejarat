package simorgh.tejarat.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simorgh.tejarat.app.services.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorsController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/add")
    public ResponseEntity<Void> addDoctor(@RequestBody String name) {
        try {
            doctorService.addDoctor(name);
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }

}
