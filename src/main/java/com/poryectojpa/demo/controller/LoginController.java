package com.poryectojpa.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.poryectojpa.demo.models.Persona;
import com.poryectojpa.demo.repository.PersonaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private PersonaRepository personaRepository;

    // GET: mostrar login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new Persona()); // usamos Persona solo para correo/password
        return "login";
    }

    // POST: procesar login
    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("loginForm") Persona persona, 
                                HttpSession session, Model model) {
        Persona usuario = personaRepository.findByEmail(persona.getEmail());

        if (usuario != null && usuario.getContraseña().equals(persona.getContraseña())) {
            // guardar datos en sesión
            session.setAttribute("usuario", usuario.getEmail());
            session.setAttribute("rol", usuario.getRolId());

            // Redirigir según rol (Usamos rutas que existen)
            if (usuario.getRolId() == null) return "redirect:/home";
            
            switch (usuario.getRolId()) {
                case 1: return "redirect:/personas"; // Dashboard de admin
                case 2: return "redirect:/home";     // Dashboard de estudiante
                default: return "redirect:/home";
            }
        } else {
            model.addAttribute("mensaje", "Usuario o contraseña incorrecta");
            return "login";
        }
    }

    // cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
