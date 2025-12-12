package com.poryectojpa.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // ← ESTA ES LA CORRECTA

import com.poryectojpa.demo.models.Curso;
import com.poryectojpa.demo.models.Estudiante;
import com.poryectojpa.demo.models.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    List<Inscripcion> findByEstudiante_IdEstudiante(Integer idEstudiante);
    //List<Inscripcion> findByIdEstudiante(Integer estudiante);
    boolean existsByEstudianteAndCurso(Estudiante estudiante, Curso curso);

}
