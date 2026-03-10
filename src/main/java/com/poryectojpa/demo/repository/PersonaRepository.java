package com.poryectojpa.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poryectojpa.demo.models.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    // Puedes agregar métodos personalizados como:
    Persona findByEmail(String email);
}
