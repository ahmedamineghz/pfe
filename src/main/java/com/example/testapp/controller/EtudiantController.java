package com.example.testapp.controller;

import com.example.testapp.entities.Cours;
import com.example.testapp.entities.Etudiant;
import com.example.testapp.services.EtudiantInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/etudiant")
@CrossOrigin(origins = "http://localhost:4200")
public class EtudiantController {

    @Autowired
    EtudiantInterface etudiantInterface;

    // ✅ Accessible par tout le monde (inscription)
    @PostMapping("/inscrire")
    public ResponseEntity<Map<String, String>> inscrireEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantInterface.inscrireEtudiant(etudiant);
    }

    // ✅ Supprimer un étudiant — accessible uniquement à ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteEtudiant(@PathVariable Long id) {
        etudiantInterface.deleteEtudiant(id);
    }

    // ✅ Mise à jour du profil — accessible uniquement à l'ETUDIANT
    @PreAuthorize("hasAuthority('ETUDIANT')")
    @PatchMapping("/update/{id}")
    public Etudiant updateEtudiant(@PathVariable("id") Long id, @RequestBody Etudiant etudiant) {
        return etudiantInterface.updateEtudiant(id, etudiant);
    }

    // ✅ Liste des étudiants — accessible uniquement à ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")
    public List<Etudiant> getAllEtudiants() {
        return etudiantInterface.getAllEtudiants();
    }

    // ✅ Récupérer un étudiant — accessible à l’étudiant ou admin
    @PreAuthorize("hasAuthority('ETUDIANT') or hasAuthority('ADMIN')")
    @GetMapping("/getById/{id}")
    public Etudiant getEtudiantById(@PathVariable Long id) {
        return etudiantInterface.getEtudiantById(id);
    }

    // ❌ À sécuriser si utilisé (ou à retirer si remplacé par /auth/login)
    @PostMapping("/authentification")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestBody) {
        String emailEtudiant = requestBody.get("emailEtudiant");
        String motPasseEtudiant = requestBody.get("motPasseEtudiant");

        String response = etudiantInterface.authenEtudiant(emailEtudiant, motPasseEtudiant);
        return ResponseEntity.ok(response);
    }

    // ✅ Inscription à un cours — accessible à l'étudiant
    @PreAuthorize("hasAuthority('ETUDIANT')")
    @PostMapping("/{etudiantId}/inscrire/{coursId}")
    public ResponseEntity<Map<String, Object>> inscrireEtudiantAuCours(@PathVariable Long etudiantId, @PathVariable Long coursId) {
        String message = etudiantInterface.inscrireEtudiantAuCours(etudiantId, coursId);

        boolean success = !message.toLowerCase().contains("déjà inscrit");

        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", message
        ));
    }


    // ✅ Liste des cours inscrits — accessible à l'étudiant
    @PreAuthorize("hasAuthority('ETUDIANT')")
    @GetMapping("/getcours/{etudiantId}")
    public ResponseEntity<List<Cours>> getCoursByEtudiant(@PathVariable Long etudiantId) {
        List<Cours> coursList = etudiantInterface.getCoursByEtudiant(etudiantId);
        return ResponseEntity.ok(coursList);
    }

    // ✅ Test token (facultatif)
    @PreAuthorize("hasAuthority('ETUDIANT')")
    @GetMapping("/test")
    public String testToken() {
        return "Accès autorisé pour l'étudiant !";
    }


    @PreAuthorize("hasAuthority('ETUDIANT')")
    @DeleteMapping("/{etudiantId}/supprimerCours/{coursId}")
    public ResponseEntity<Map<String, Object>> supprimerCoursEtudiant(
            @PathVariable Long etudiantId,
            @PathVariable Long coursId) {

        String message = etudiantInterface.supprimerCoursEtudiant(etudiantId, coursId);

        boolean success = message.toLowerCase().contains("succès");

        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", message
        ));
    }
}
