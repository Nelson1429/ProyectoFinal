package com.poryectojpa.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poryectojpa.demo.dto.DatoEstadisticoDTO;
import com.poryectojpa.demo.Service.ReporteJasperService;
import com.poryectojpa.demo.repository.personaRepository;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteJasperService reporteJasperService;
    private final personaRepository personaRepository;
    private final JdbcTemplate jdbcTemplate;

    public ReporteController(ReporteJasperService reporteJasperService,
                             personaRepository personaRepository,
                             JdbcTemplate jdbcTemplate) {
        this.reporteJasperService = reporteJasperService;
        this.personaRepository = personaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------
    // 1. Vista Thymeleaf
    // -------------------------------
    @GetMapping
    public String mostrarVistaReporte(Model model) {

        String sql = "SELECT genero AS label, COUNT(*) AS valor FROM persona GROUP BY genero";

        List<DatoEstadisticoDTO> datos = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new DatoEstadisticoDTO(
                        rs.getString("label"),
                        rs.getDouble("valor")
                )
        );

        model.addAttribute("datos", datos);

        return "vistaReporteEstadistico";
    }

    // -------------------------------
    // 2. Generar PDF directamente
    // -------------------------------
    @GetMapping("/estadistico/pdf")
    public void generarReporteEstadistico(HttpServletResponse response) {
        try {

            String sql = "SELECT genero AS label, COUNT(*) AS valor FROM persona GROUP BY genero";

            List<DatoEstadisticoDTO> datos = jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> new DatoEstadisticoDTO(
                            rs.getString("label"),
                            rs.getDouble("valor")
                    )
            );

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte estadístico de personas");
            parametros.put("NUMEROPERSONAS", "Total Personas registradas: " + datos.stream().mapToDouble(DatoEstadisticoDTO::getValor).sum());

            byte[] pdfBytes = reporteJasperService.generarReporteEstadisticoPdf(datos, parametros);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_estadistico.pdf");
            response.setContentLength(pdfBytes.length);
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }
}
