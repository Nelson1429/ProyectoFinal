package com.poryectojpa.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.poryectojpa.demo.models.Curso;
import com.poryectojpa.demo.models.EstadoInscripcion;
import com.poryectojpa.demo.models.Inscripcion;
import com.poryectojpa.demo.repository.InscripcionRepository;
import com.poryectojpa.demo.repository.cursoRepository;
import com.poryectojpa.demo.repository.EstadoInscripcionRepository;

@Controller
@RequestMapping("/inscripciones")
public class InscripcionController {

    @Autowired
    private cursoRepository cursoRepository;

    @Autowired
    private EstadoInscripcionRepository estadoRepo;

    @Autowired
    private InscripcionRepository inscripcionRepo;

    // ----------- MOSTRAR FORMULARIO -----------------
    @GetMapping({"/nueva", "/nueva/{idEstudiante}"})
    public String nuevaInscripcion(@PathVariable(required = false) Integer idEstudiante, Model model) {

        model.addAttribute("idEstudiante", idEstudiante);
        model.addAttribute("listaCursos", cursoRepository.findAll());
        model.addAttribute("listaEstados", estadoRepo.findAll());

        return "inscripcion"; // inscripcion.html en templates
    }

    // ------------ GUARDAR INSCRIPCIÓN ----------------
    @PostMapping("/guardar")
    public String guardarInscripcion(
            @RequestParam Integer idEstudiante,
            @RequestParam Integer idCurso,
            @RequestParam Integer idEstado) {

        Inscripcion insc = new Inscripcion();
        insc.setIdEstudiante(idEstudiante);

        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        EstadoInscripcion estado = estadoRepo.findById(idEstado).orElse(null);

        insc.setCurso(curso);
        insc.setEstado(estado);

        inscripcionRepo.save(insc);

        return "redirect:/cursos"; // redirige a la lista de cursos
    }
}
