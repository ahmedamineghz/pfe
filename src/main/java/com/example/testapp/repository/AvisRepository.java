package com.example.testapp.repository;

import com.example.testapp.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByCours_IdCour(Long coursId);
}
