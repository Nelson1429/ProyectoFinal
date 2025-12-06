package com.poryectojpa.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poryectojpa.demo.models.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
}
