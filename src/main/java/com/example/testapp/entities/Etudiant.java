package com.example.testapp.entities;

import com.example.testapp.serializer.EtudiantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "etudiant")
@JsonSerialize( using = EtudiantSerializer.class)
public class Etudiant {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idEtudiant;
        @Column(length = 20,nullable = true)
        private  String nomEtudiant;
        private  String emailEtudiant;
        private  String motPasseEtudiant;
        private Role role = Role.ETUDIANT;
        @Column(nullable = false)
        private boolean active = false;



        public Long getIdEtudiant() {
                return idEtudiant;
        }

        public String getNomEtudiant() {
                return nomEtudiant;
        }

        public String getEmailEtudiant() {
                return emailEtudiant;
        }

        public String getMotPasseEtudiant() {
                return motPasseEtudiant;
        }

        public List<Avis> getAvis() {
                return avis;
        }

        public List<Chat> getChats() {
                return chats;
        }

        public void setIdEtudiant(Long idEtudiant) {
                this.idEtudiant = idEtudiant;
        }

        public void setNomEtudiant(String nomEtudiant) {
                this.nomEtudiant = nomEtudiant;
        }

        public void setEmailEtudiant(String emailEtudiant) {
                this.emailEtudiant = emailEtudiant;
        }

        public void setMotPasseEtudiant(String motPasseEtudiant) {
                this.motPasseEtudiant = motPasseEtudiant;
        }

        public void setAvis(List<Avis> avis) {
                this.avis = avis;
        }

        public void setChats(List<Chat> chats) {
                this.chats = chats;
        }

        public void setCoursInscrits(List<Cours> coursInscrits) {
                this.coursInscrits = coursInscrits;
        }

        public Role getRole() {
                return role;
        }

        public void setRole(Role role) {
                this.role = role;
        }

        public boolean isActive() {
                return active;
        }

        public void setActive(boolean active) {
                this.active = active;
        }

        public List<Cours> getCoursInscrits() {
                return coursInscrits;
        }

        @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
        private List<Avis> avis;
        @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
        private List<Chat> chats;
        @ManyToMany
        @JoinTable(
                name = "etudiant_cours",
                joinColumns = @JoinColumn(name = "etudiant_id"),
                inverseJoinColumns = @JoinColumn(name = "cours_id")
        )
        private List<Cours> coursInscrits;


}
