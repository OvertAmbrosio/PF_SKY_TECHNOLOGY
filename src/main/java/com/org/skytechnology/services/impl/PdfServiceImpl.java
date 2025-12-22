package com.org.skytechnology.services.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.org.skytechnology.entity.*;
import com.org.skytechnology.services.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    private static final String SEPARATOR = "───────────────────────────────────────────────────";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public ByteArrayOutputStream generarBoleta(Orden orden) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        log.info("Generando boleta PDF para la Orden Nº: {}", orden.getId());

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("SKY TECHNOLOGY")
                    .setFontSize(24).setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.BLUE));

            document.add(new Paragraph("BOLETA DE VENTA ELECTRÓNICA")
                    .setFontSize(16).setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.add(new Paragraph(SEPARATOR).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Nº Orden: " + String.format("%06d", orden.getId())).setBold());
            document.add(new Paragraph("Fecha: " + orden.getFechaCreacion().format(DATE_FORMAT)));
            document.add(new Paragraph("Estado: " + orden.getEstadoPago()).setMarginBottom(10));

            document.add(new Paragraph("DATOS DEL CLIENTE").setBold().setMarginTop(10));
            document.add(new Paragraph("Cliente: " + orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido()));
            document.add(new Paragraph("Email: " + orden.getUsuario().getEmail()).setMarginBottom(10));

            document.add(new Paragraph(SEPARATOR).setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 2, 2, 2})).useAllAvailableWidth();
            
            String[] headers = {"Cant.", "Producto", "Categoría", "P. Unit.", "Subtotal"};
            for (String h : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(h).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
            }

            for (DetalleOrden detalle : orden.getDetalles()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detalle.getCantidad()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(detalle.getProducto().getNombre())));
                table.addCell(new Cell().add(new Paragraph(detalle.getProducto().getCategoria().getNombre())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(String.format("S/ %.2f", detalle.getPrecioUnitario()))).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(String.format("S/ %.2f", detalle.getSubtotal()))).setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(table);

            // Total
            document.add(new Paragraph("TOTAL: S/ " + String.format("%.2f", orden.getTotal()))
                    .setFontSize(16).setBold().setTextAlignment(TextAlignment.RIGHT).setMarginTop(15));

            document.add(new Paragraph(SEPARATOR).setTextAlignment(TextAlignment.CENTER).setMarginTop(20));
            document.add(new Paragraph("¡Gracias por su compra!").setTextAlignment(TextAlignment.CENTER).setItalic());

        } catch (Exception e) {
            log.error("Error al construir el PDF para la orden {}: {}", orden.getId(), e.getMessage());
            throw new RuntimeException("Error técnico al generar el comprobante PDF", e);
        }

        return baos;
    }

    @Override
    public ByteArrayOutputStream generarReporteOrdenes(List<Orden> ordenes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("REPORTE DE VENTAS - SKY TECHNOLOGY")
                    .setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3, 2, 2})).useAllAvailableWidth();
            String[] headers = {"N°", "Fecha", "Cliente", "Total (S/)", "Estado"};
            
            for (String h : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(h).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
            }

            int i = 1;
            for (Orden o : ordenes) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(i++))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(o.getFechaCreacion().format(DATE_FORMAT))));
                table.addCell(new Cell().add(new Paragraph(o.getUsuario().getNombre() + " " + o.getUsuario().getApellido())));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", o.getTotal()))).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(o.getEstadoPago().toString())).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(table);
            document.add(new Paragraph("\nTOTAL ÓRDENES: " + ordenes.size()).setBold().setTextAlignment(TextAlignment.RIGHT));

        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de órdenes", e);
        }
        return baos;
    }

    @Override
    public ByteArrayOutputStream exportarProductosPdf(List<Producto> productos) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("INVENTARIO DE PRODUCTOS")
                    .setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER).setMarginBottom(20));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 3, 2, 1})).useAllAvailableWidth();
            String[] headers = {"ID", "Nombre", "Categoría", "Precio", "Stock"};

            for (String h : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(h).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
            }

            for (Producto p : productos) {
                table.addCell(new Cell().add(new Paragraph(p.getId().toString())).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(p.getNombre())));
                table.addCell(new Cell().add(new Paragraph(p.getCategoria().getNombre())));
                table.addCell(new Cell().add(new Paragraph(String.format("S/ %.2f", p.getPrecio()))).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(p.getStock()))).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de inventario", e);
        }
        return baos;
    }
}