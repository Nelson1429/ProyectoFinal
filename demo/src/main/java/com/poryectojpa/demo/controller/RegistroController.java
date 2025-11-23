package com.poryectojpa.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.poryectojpa.demo.models.Persona;
import com.poryectojpa.demo.repository.personaRepository;

import jakarta.validation.Valid;

@Controller
public class RegistroController {

    @Autowired
    private personaRepository personaRepository;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("persona", new Persona());
        return "registro"; // vista registro.html
    }

    @PostMapping("/registro")
    public String procesarFormulario(
            @Valid @ModelAttribute("persona") Persona persona,
            BindingResult result,
            Model model) {

        // Si hay errores de validación, volver al formulario
        if (result.hasErrors()) {
            return "registro";
        }

        // Verificar si ya existe el correo
        if (personaRepository.findByEmail(persona.getEmail()) != null) {
            model.addAttribute("errorCorreo", "El correo ya está registrado");
            return "registro";
        }

        // Guardar persona
        personaRepository.save(persona);

        // Redirigir a login
        return "redirect:/login";
    }
}
