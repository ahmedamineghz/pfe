package com.example.testapp.config;

import com.example.testapp.entities.Admin;
import com.example.testapp.entities.Etudiant;
import com.example.testapp.entities.Tuteur;
import com.example.testapp.repository.AdminRepository;
import com.example.testapp.repository.EtudiantRepository;
import com.example.testapp.repository.TuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Recherche de l'utilisateur : " + email);

        Etudiant etu = etudiantRepository.findByEmailEtudiant(email).orElse(null);
        if (etu != null) {
            if (!etu.isActive()) {
                System.out.println("⛔ Étudiant non activé !");
                throw new UsernameNotFoundException("Votre compte étudiant n'est pas encore activé. Veuillez contacter l'administrateur.");
            }
            System.out.println("✅ Étudiant trouvé et activé !");
            return new User(
                    etu.getEmailEtudiant(),
                    etu.getMotPasseEtudiant(),
                    Collections.singletonList(new SimpleGrantedAuthority("ETUDIANT"))
            );
        }

        Tuteur tut = tuteurRepository.findByEmailTuteur(email).orElse(null);
        if (tut != null) {
            if (!tut.isActive()) {
                System.out.println("⛔ Tuteur non activé !");
                throw new UsernameNotFoundException("Votre compte tuteur n'est pas encore activé. Veuillez contacter l'administrateur.");
            }
            System.out.println("✅ Tuteur trouvé et activé !");
            return new User(
                    tut.getEmailTuteur(),
                    tut.getMotPasseTuteur(),
                    Collections.singletonList(new SimpleGrantedAuthority("TUTEUR"))
            );
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            System.out.println("✅ Admin trouvé !");
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
            );
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé !");
    }
}
