package com.healthconnect.userservice.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class EmailUtils {
    public static String convertImageToBase64(String imagePath) throws IOException {
        InputStream imageStream = new ClassPathResource(imagePath).getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(imageStream);
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
