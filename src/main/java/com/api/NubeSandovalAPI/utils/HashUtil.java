package com.api.NubeSandovalAPI.utils;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.MessageDigest;

@Component
public class HashUtil {

    public String calcularSHA256(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }

            byte[] hashBytes = digest.digest();

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error calculando hash");
        }
    }
}
