package com.poryectojpa.demo.controller;

import com.poryectojpa.demo.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/correo")
public class CorreoController {

    @Autowired
    private EmailService emailService;

    // ============================================================
    // 1️⃣ VISTA THYMELEAF
    // ============================================================
    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "correoFormulario"; // ARCHIVO HTML QUE ME PASASTE
    }

    // ============================================================
    // 2️⃣ ENVÍO DESDE LA VISTA
    // ============================================================
    @PostMapping("/enviar-desde-vista")
    public String enviarDesdeVista(
            @RequestParam String para,
            @RequestParam String asunto,
            @RequestParam String mensaje,
            @RequestParam String tipo,
            RedirectAttributes redirectAttributes) {

        try {
            if (tipo.equals("texto")) {
                emailService.enviarTexto(para, asunto, mensaje);

            } else if (tipo.equals("html")) {

                String html = """
                        <div style="font-family: Arial; padding: 20px; background: #fdf2f8; border-radius: 15px;">
                            <h2 style="color:#d946ef; text-align:center;">Mensaje HTML</h2>

                            <p style="font-size:16px; color:#444;">
                                %s
                            </p>

                            <p style="text-align:center; color:#999; font-size:13px; margin-top:25px;">
                                Enviado desde el formulario
                            </p>
                        </div>
                        """.formatted(mensaje);

                emailService.enviarHtml(para, asunto, html);
            }

            redirectAttributes.addFlashAttribute("mensaje", "Correo enviado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/correo/formulario";
    }

    // ============================================================
    // 3️⃣ ENDPOINTS PARA POSTMAN (API)
    // ============================================================
    @ResponseBody
    @PostMapping("/texto")
    public ResponseEntity<String> enviarTexto(
            @RequestParam String para,
            @RequestParam String asunto,
            @RequestParam String mensaje) {

        emailService.enviarTexto(para, asunto, mensaje);
        return ResponseEntity.ok("Correo enviado correctamente (texto plano).");
    }

    @ResponseBody
    @PostMapping("/html")
    public ResponseEntity<String> enviarHtml(
            @RequestParam String para,
            @RequestParam String asunto) throws MessagingException {

        String html = generarPlantillaHtml(para);
        emailService.enviarHtml(para, asunto, html);

        return ResponseEntity.ok("Correo enviado correctamente (HTML).");
    }

    @ResponseBody
    @PostMapping("/adjunto")
    public ResponseEntity<String> enviarAdjunto(
            @RequestParam String para,
            @RequestParam String asunto,
            @RequestParam String mensaje,
            @RequestParam String archivo) throws MessagingException {

        emailService.enviarConAdjunto(para, asunto, mensaje, archivo);
        return ResponseEntity.ok("Correo enviado con adjunto.");
    }

    // ============================================================
    // 4️⃣ ENDPOINTS DE PRUEBA (GET)
    // ============================================================
    @ResponseBody
    @GetMapping("/test/texto")
    public String pruebaTexto() {
        try {
            emailService.enviarTexto(
                    "sabormasterclass@gmail.com",
                    "Prueba Texto",
                    "Este es un mensaje de prueba"
            );
            return "✔ Correo (texto) enviado correctamente";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/test/html")
    public String pruebaHtml() {
        try {
            emailService.enviarHtml(
                    "TU_CORREO@gmail.com",
                    "Prueba HTML",
                    "<h1 style='color:blue;'>Correo HTML de prueba</h1>"
            );
            return "✔ Correo (HTML) enviado correctamente";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @ResponseBody
    @GetMapping("/test/adjunto")
    public String pruebaAdjunto() {
        try {
            emailService.enviarConAdjunto(
                    "TU_CORREO@gmail.com",
                    "Prueba Adjunto",
                    "Este correo contiene un archivo adjunto.",
                    "C:/ruta/archivo.pdf"
            );
            return "✔ Correo (adjunto) enviado correctamente";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    // ============================================================
    // 5️⃣ PLANTILLA HTML
    // ============================================================
    private String generarPlantillaHtml(String nombre) {
        return """
                <div style="font-family: Arial; padding: 20px; background: #fdf2f8; border-radius: 15px;">
                    <h2 style="color:#d946ef; text-align:center;">¡Bienvenido!</h2>

                    <p style="font-size:16px; color:#444;">
                        Hola <b>%s</b>, estamos felices de que hagas parte de nuestra plataforma.
                    </p>

                    <div style="background:#fff; padding:15px; border-radius:10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                        <p style="color:#555;">
                            Gracias por registrarte. Desde hoy podrás acceder a nuestros servicios y contenido.
                        </p>
                    </div>

                    <p style="text-align:center; color:#999; font-size:13px; margin-top:25px;">
                        © 2025 - Todos los derechos reservados
                    </p>
                </div>
                """.formatted(nombre);
    }
}
