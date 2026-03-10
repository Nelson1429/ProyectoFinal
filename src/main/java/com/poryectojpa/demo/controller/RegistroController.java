package com.poryectojpa.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.beans.factory.annotation.Autowired;
import com.poryectojpa.demo.models.Persona;
import com.poryectojpa.demo.repository.PersonaRepository;

@Controller
public class RegistroController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("persona", new Persona());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormulario(@ModelAttribute Persona persona) {
        // Asignamos valores obligatorios para evitar errores en la BD
        if (persona.getRolId() == null) {
            persona.setRolId(2); // 2 = Estudiante por defecto
        }
        if (persona.getDireccion() == null) {
            persona.setDireccion("Pendiente");
        }
        
        personaRepository.save(persona);
        return "redirect:/login";
    }
}

