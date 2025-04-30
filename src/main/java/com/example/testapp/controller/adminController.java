package com.example.testapp.controller;

import com.example.testapp.entities.Etudiant;
import com.example.testapp.entities.Tuteur;
import com.example.testapp.repository.EtudiantRepository;
import com.example.testapp.repository.TuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("admin")

public class adminController {
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    TuteurRepository tuteurRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/activer-etudiant/{id}")
    public ResponseEntity<?> activerEtudiant(@PathVariable Long id) {
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(id);
        if (optionalEtudiant.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Étudiant introuvable."));
        }

        Etudiant etu = optionalEtudiant.get();
        etu.setActive(true);
        etudiantRepository.save(etu);

        return ResponseEntity.ok(Collections.singletonMap("message", "Compte étudiant activé avec succès."));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/activer-tuteur/{id}")
    public ResponseEntity<?> activerTuteur(@PathVariable Long id) {
        Optional<Tuteur> optionalTuteur = tuteurRepository.findById(id);
        if (optionalTuteur.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Tuteur introuvable."));
        }

        Tuteur tut = optionalTuteur.get();
        tut.setActive(true);
        tuteurRepository.save(tut);

        return ResponseEntity.ok(Collections.singletonMap("message", "Compte tuteur activé avec succès."));
    }

}
