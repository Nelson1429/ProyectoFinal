package com.poryectojpa.demo.repository;

import com.poryectojpa.demo.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {}
