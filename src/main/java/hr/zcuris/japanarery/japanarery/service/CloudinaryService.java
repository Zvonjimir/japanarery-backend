package hr.zcuris.japanarery.japanarery.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "activities")
        );
        return (String) result.get("secure_url");
    }

    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isBlank()) {
            log.warn("deleteImage: URL je null ili prazan, preskačem");
            return;
        }

        if (!imageUrl.contains("cloudinary.com")) {
            log.warn("deleteImage: URL nije Cloudinary ({}) — preskačem brisanje", imageUrl);
            return;
        }

        String publicId = extractPublicId(imageUrl);
        if (publicId == null) {
            log.warn("deleteImage: nije moguće izvući publicId iz URL-a: {}", imageUrl);
            return;
        }

        log.info("deleteImage: brišem publicId '{}'", publicId);
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        log.info("deleteImage: Cloudinary odgovor: {}", result.get("result"));
    }

    private String extractPublicId(String url) {
        // Cloudinary URL format:
        // https://res.cloudinary.com/<cloud>/image/upload/v<version>/<folder>/<filename>.<ext>
        // https://res.cloudinary.com/<cloud>/image/upload/<folder>/<filename>.<ext>  (bez verzije)
        if (!url.contains("/upload/")) {
            return null;
        }

        String afterUpload = url.split("/upload/")[1];

        // Ukloni version prefix (v1234567/)
        afterUpload = afterUpload.replaceFirst("^v\\d+/", "");

        // Ukloni ekstenziju
        int dotIndex = afterUpload.lastIndexOf('.');
        if (dotIndex != -1) {
            afterUpload = afterUpload.substring(0, dotIndex);
        }

        return afterUpload.isBlank() ? null : afterUpload;
    }
}