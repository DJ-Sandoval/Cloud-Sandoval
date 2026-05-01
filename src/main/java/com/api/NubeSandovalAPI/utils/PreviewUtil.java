package com.api.NubeSandovalAPI.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PreviewUtil {

    private static final int PREVIEW_WIDTH = 800;
    private static final int PREVIEW_HEIGHT = 600;
    private static final float PDF_RENDER_QUALITY = 1.5f;

    /**
     * Genera preview según el tipo MIME
     */
    public byte[] generarPreview(Path rutaArchivo, String mimeType) {

        if (mimeType == null) {
            return generarPlaceholder("Tipo de archivo desconocido");
        }

        String type = mimeType.toLowerCase();

        try {
            if (type.startsWith("image/")) {
                return generarThumbnailImagen(rutaArchivo);

            } else if (type.equals("application/pdf")) {
                return previewPdf(rutaArchivo);

            } else if (type.contains("excel") || type.contains("spreadsheet")) {
                return previewExcel(rutaArchivo);

            } else if (type.startsWith("text/") ||
                    type.contains("json") ||
                    type.contains("xml") ||
                    type.contains("javascript") ||
                    type.contains("css")) {
                return previewTexto(rutaArchivo);
            }

        } catch (Exception e) {
            return generarPlaceholder("Error al generar preview: " + e.getMessage());
        }

        return generarPlaceholder("Tipo de archivo no soportado");
    }

    /**
     * Preview PDF
     */
    private byte[] previewPdf(Path pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {

            if (document.getNumberOfPages() == 0) {
                return generarPlaceholder("PDF sin páginas");
            }

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0, PDF_RENDER_QUALITY);

            BufferedImage scaled = escalarImagen(image, PREVIEW_WIDTH, PREVIEW_HEIGHT);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaled, "PNG", baos);

            return baos.toByteArray();
        }
    }

    /**
     * Preview Excel
     */
    private byte[] previewExcel(Path excelPath) throws IOException {

        try (InputStream is = Files.newInputStream(excelPath)) {

            Workbook workbook;
            String fileName = excelPath.getFileName().toString().toLowerCase();

            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                return generarPlaceholder("Formato Excel no soportado");
            }

            if (workbook.getNumberOfSheets() == 0) {
                workbook.close();
                return generarPlaceholder("Excel sin hojas");
            }

            Sheet sheet = workbook.getSheetAt(0);

            StringBuilder html = new StringBuilder();
            html.append("<div class='overflow-auto max-h-[80vh] bg-white rounded-lg'>");
            html.append("<table class='min-w-full border-collapse text-sm'>");

            int maxRows = Math.min(sheet.getPhysicalNumberOfRows(), 50);

            for (int i = 0; i < maxRows; i++) {

                Row row = sheet.getRow(i);

                html.append("<tr class='")
                        .append(i == 0 ? "bg-gray-100 font-bold" : "hover:bg-gray-50")
                        .append("'>");

                int maxCols = row != null ? Math.min(row.getLastCellNum(), 20) : 1;

                for (int j = 0; j < maxCols; j++) {
                    Cell cell = row != null ? row.getCell(j) : null;

                    html.append("<td class='border px-3 py-2'>")
                            .append(escapeHtml(obtenerValorCelda(cell)))
                            .append("</td>");
                }

                html.append("</tr>");
            }

            html.append("</table></div>");

            workbook.close();
            return html.toString().getBytes("UTF-8");
        }
    }

    /**
     * Obtener valor celda
     */
    private String obtenerValorCelda(Cell cell) {

        if (cell == null) return "";

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();

                case NUMERIC -> {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                    }

                    double num = cell.getNumericCellValue();

                    if (num == Math.floor(num)) {
                        yield String.format("%.0f", num);
                    }

                    yield String.valueOf(num);
                }

                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());

                case FORMULA -> cell.getCellFormula();

                default -> "";
            };

        } catch (Exception e) {
            return "Error";
        }
    }

    /**
     * Imagen preview
     */
    private byte[] generarThumbnailImagen(Path imagePath) throws IOException {

        try (InputStream is = Files.newInputStream(imagePath)) {

            BufferedImage original = ImageIO.read(is);

            if (original == null) {
                return generarPlaceholder("Formato de imagen no soportado");
            }

            BufferedImage scaled = escalarImagen(original, PREVIEW_WIDTH, PREVIEW_HEIGHT);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaled, "PNG", baos);

            return baos.toByteArray();
        }
    }

    /**
     * Preview texto
     */
    private byte[] previewTexto(Path filePath) throws IOException {

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {

            String line;
            int count = 0;

            while ((line = reader.readLine()) != null && count < 500) {
                content.append(escapeHtml(line)).append("\n");
                count++;
            }
        }

        String html = "<pre class='bg-black text-green-400 p-4 rounded'>"
                + content
                + "</pre>";

        return html.getBytes("UTF-8");
    }

    /**
     * Placeholder
     */
    private byte[] generarPlaceholder(String mensaje) {

        String html = "<div style='text-align:center;padding:40px;'>"
                + "<p style='color:gray'>" + escapeHtml(mensaje) + "</p>"
                + "</div>";

        return html.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * Escalar imagen
     */
    private BufferedImage escalarImagen(BufferedImage original, int maxWidth, int maxHeight) {

        int width = original.getWidth();
        int height = original.getHeight();

        double scale = Math.min(
                (double) maxWidth / width,
                (double) maxHeight / height
        );

        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return scaled;
    }

    /**
     * Escape HTML
     */
    private String escapeHtml(String text) {

        if (text == null) return "";

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
