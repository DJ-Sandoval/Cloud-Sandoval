package com.api.NubeSandovalAPI.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ThumbnailUtil {

    public byte[] generarThumbnail(Path rutaArchivo, int width, int height) {

        try (InputStream is = Files.newInputStream(rutaArchivo)) {

            BufferedImage original = ImageIO.read(is);

            if (original == null) {
                throw new RuntimeException("Formato no soportado para miniatura");
            }

            BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = thumbnail.createGraphics();

            // 🔥 Mejora calidad
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);

            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generando thumbnail", e);
        }
    }
}
