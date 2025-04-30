package com.example.testapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChat;
    private String messageChat;
    @Temporal(TemporalType.DATE)
    private Date dateChat;

    public Long getIdChat() {
        return idChat;
    }

    public String getMessageChat() {
        return messageChat;
    }

    public Date getDateChat() {
        return dateChat;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public Tuteur getTuteur() {
        return tuteur;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public void setMessageChat(String messageChat) {
        this.messageChat = messageChat;
    }

    public void setDateChat(Date dateChat) {
        this.dateChat = dateChat;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "tuteur_id")
    private Tuteur tuteur;
}
