package com.sape.safety_for_people.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("La imagen es obligatoria");
        }

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new IllegalArgumentException(
                    "El archivo debe ser una imagen"
            );
        }

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "safety-for-people/products",
                        "resource_type", "image",
                        "use_filename", true,
                        "unique_filename", true
                )
        );

        return result.get("secure_url").toString();
    }
}