package com.example.testapp.controller;

import com.example.testapp.entities.Etudiant;
import com.example.testapp.entities.Tuteur;
import com.example.testapp.services.TuteurInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tuteur")
@CrossOrigin(origins = "http://localhost:4200")
public class TuteurController {

    @Autowired
    TuteurInterface tuteurInterface;

    @PostMapping("/inscrire")
    public ResponseEntity<Map<String, Object>> inscrireTuteur(@RequestBody Tuteur tuteur) {
        String message = tuteurInterface.inscrireTuteur(tuteur);
        boolean success = !message.toLowerCase().contains("déjà");
        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", message
        ));
    }

    @PostMapping("/authentification")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> requestBody) {
        String emailTuteur = requestBody.get("emailTuteur");
        String motPasseTuteur = requestBody.get("motPasseTuteur");

        String response = tuteurInterface.authenTuteur(emailTuteur, motPasseTuteur);
        boolean success = !response.toLowerCase().contains("incorrect") && !response.toLowerCase().contains("introuvable");

        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", response
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteTutuer(@PathVariable Long id) {
        tuteurInterface.deleteTuteur(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tuteur supprimé avec succès."
        ));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateTuteur(@PathVariable Long id, @RequestBody Tuteur tuteur) {
        Tuteur updated = tuteurInterface.updateTuteur(id, tuteur);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tuteur mis à jour avec succès.",
                "tuteur", updated
        ));
    }

    @GetMapping("/findAll")
    public ResponseEntity<Map<String, Object>> getAllTuteurs() {
        List<Tuteur> tuteurs = tuteurInterface.getAllTuteurs();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", tuteurs
        ));
    }

    @GetMapping("/finById/{id}")
    public ResponseEntity<Map<String, Object>> getTuteurById(@PathVariable Long id) {
        Tuteur tuteur = tuteurInterface.getTuteurById(id);
        return ResponseEntity.ok(Map.of(
                "success", tuteur != null,
                "data", tuteur
        ));
    }

    @GetMapping("/coursPublier/{idTuteur}")
    public ResponseEntity<Map<String, Object>> getCoursPublies(@PathVariable Long idTuteur) {
        Object response = tuteurInterface.getCoursPubliesByTuteur(idTuteur);
        boolean success = !(response instanceof String && ((String) response).toLowerCase().contains("aucun"));

        return ResponseEntity.ok(Map.of(
                "success", success,
                "data", response
        ));
    }
}
