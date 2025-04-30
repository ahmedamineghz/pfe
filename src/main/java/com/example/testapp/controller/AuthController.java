package com.example.testapp.controller;


import com.example.testapp.config.JwtUtil;
import com.example.testapp.dto.AuthRequest;
import com.example.testapp.dto.AuthResponse;
import com.example.testapp.dto.RegisterResponse;
import com.example.testapp.entities.Admin;
import com.example.testapp.entities.Etudiant;
import com.example.testapp.entities.Role;
import com.example.testapp.entities.Tuteur;
import com.example.testapp.repository.AdminRepository;
import com.example.testapp.repository.EtudiantRepository;
import com.example.testapp.repository.TuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return new AuthResponse(false, "Email ou mot de passe manquant.", null, null, null,null);
        }

        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmailEtudiant(email);
        Optional<Tuteur> tuteurOpt = tuteurRepository.findByEmailTuteur(email);
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        Long id;
        String role;
        String username;
        String token;

        try {
            if (etudiantOpt.isPresent()) {
                Etudiant etudiant = etudiantOpt.get();
                if (!etudiant.isActive()) {
                    return new AuthResponse(false, "Votre compte étudiant n'est pas encore activé. Veuillez vérifier votre email ou contacter l'administrateur.", null, null, null,null);
                }
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                role = "ETUDIANT";
                username = etudiant.getNomEtudiant(); // nom de l'étudiant
                id = etudiant.getIdEtudiant();
                token = jwtUtil.generateToken(email, role);
                return new AuthResponse(true, "Connexion réussie", token, role, username, id);
            }

            if (tuteurOpt.isPresent()) {
                Tuteur tuteur = tuteurOpt.get();
                if (!tuteur.isActive()) {
                    return new AuthResponse(false, "Votre compte tuteur n'est pas encore activé. Veuillez vérifier votre email ou contacter l'administrateur.", null, null, null,null);
                }
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                role = "TUTEUR";
                username = tuteur.getNomTuteur(); // nom du tuteur
                id = tuteur.getIdTuteur();
                token = jwtUtil.generateToken(email, role);
                return new AuthResponse(true, "Connexion réussie", token, role, username,id);
            }

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                id = admin.getId();
                role = "ADMIN";// nom de l'admin — adapte selon ta classe Admin
                token = jwtUtil.generateToken(email, role);
                return new AuthResponse(true, "Connexion réussie", token, role, null,id);
            }

            return new AuthResponse(false, "Aucun utilisateur trouvé avec cet email.", null, null, null,null);

        } catch (BadCredentialsException e) {
            return new AuthResponse(false, "Mot de passe incorrect.", null, null, null,null);
        } catch (Exception e) {
            return new AuthResponse(false, "Erreur interne : " + e.getMessage(), null, null, null,null);
        }
    }




    // ✅ Pour créer un compte étudiant (exemple)
    @PostMapping("/register/etudiant")
    public RegisterResponse registerEtudiant(@RequestBody Etudiant etudiant) {
        if (etudiant.getEmailEtudiant() == null || etudiant.getMotPasseEtudiant() == null ||
                etudiant.getEmailEtudiant().isEmpty() || etudiant.getMotPasseEtudiant().isEmpty()) {
            return new RegisterResponse(false, "Email ou mot de passe manquant.");
        }

        if (etudiantRepository.findByEmailEtudiant(etudiant.getEmailEtudiant()).isPresent()) {
            return new RegisterResponse(false, "Email déjà utilisé.");
        }

        etudiant.setMotPasseEtudiant(passwordEncoder.encode(etudiant.getMotPasseEtudiant()));
        etudiant.setRole(Role.ETUDIANT);
        etudiant.setActive(false); // Compte inactif à la création
        etudiantRepository.save(etudiant);

        return new RegisterResponse(
                true,
                "Inscription réussie. Votre compte sera activé par l'administrateur après validation du paiement. Veuillez vérifier votre email."
        );
    }



    // ✅ Pour créer un compte tuteur (exemple)
    @PostMapping("/register/tuteur")
    public RegisterResponse registerTuteur(@RequestBody Tuteur tuteur) {
        if (tuteur.getEmailTuteur() == null || tuteur.getMotPasseTuteur() == null ||
                tuteur.getEmailTuteur().isEmpty() || tuteur.getMotPasseTuteur().isEmpty()) {
            return new RegisterResponse(false, "Email ou mot de passe manquant.");
        }

        if (tuteurRepository.findByEmailTuteur(tuteur.getEmailTuteur()).isPresent()) {
            return new RegisterResponse(false, "Email déjà utilisé.");
        }

        tuteur.setMotPasseTuteur(passwordEncoder.encode(tuteur.getMotPasseTuteur()));
        tuteur.setRole(Role.TUTEUR);
        tuteur.setActive(false); // compte inactif à la création
        tuteurRepository.save(tuteur);

        return new RegisterResponse(
                true,
                "Inscription réussie. Votre compte sera activé par l'administrateur après validation du paiement. Veuillez vérifier votre email."
        );
    }



    @PostMapping("/register/admin")
    public RegisterResponse registerAdmin(@RequestBody Admin admin) {
        if (admin.getEmail() == null || admin.getPassword() == null ||
                admin.getEmail().isEmpty() || admin.getPassword().isEmpty()) {
            return new RegisterResponse(false, "Email ou mot de passe manquant.");
        }

        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            return new RegisterResponse(false, "Email déjà utilisé.");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);

        return new RegisterResponse(true, "Admin enregistré avec succès.");
    }


}