package com.poryectojpa.demo.Service;
import com.poryectojpa.demo.dto.DatoEstadisticoDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteJasperService {

    public byte[] generarReporteEstadisticoPdf(
            List<DatoEstadisticoDTO> datos,
            Map<String, Object> parametros
    ) {
        try {
            // OJO: sin pasar la ruta desde afuera,
            // la dejamos fija y bien escrita
            InputStream jrxmlStream = getClass()
                    .getResourceAsStream("/Reportes/Reporte_Estadistico.jrxml");

            if (jrxmlStream == null) {
                throw new RuntimeException("No se encontró la plantilla: /Reportes/Reporte_Estadistico.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando el reporte Jasper: " + e.getMessage(), e);
        }
    }
}