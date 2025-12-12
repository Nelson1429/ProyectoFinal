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
import jakarta.persistence.Transient;

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

        // Validaciones de Spring
        if (result.hasErrors()) {
            return "registro";
        }

        // Validar si el correo ya existe
        if (personaRepository.findByEmail(persona.getEmail()) != null) {
            model.addAttribute("errorCorreo", "El correo ya está registrado");
            return "registro";
        }

        // Validar confirmación de contraseña
        if (!persona.getContrasena().equals(persona.getConfirmarContrasena())) {
            model.addAttribute("errorContrasena", "Las contraseñas no coinciden");
            return "registro";
        }

        // Validar aceptación de términos
        if (persona.getAceptaTerminos() == null || !persona.getAceptaTerminos()) {
            model.addAttribute("errorTerminos", "Debes aceptar los términos y condiciones");
            return "registro";
        }

        // Guardar persona en la base de datos
        personaRepository.save(persona);

        // Redirigir al login
        return "redirect:/login";
    }
}
