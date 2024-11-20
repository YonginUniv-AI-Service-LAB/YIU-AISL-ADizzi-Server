package AISL.ADizzi.service;

import AISL.ADizzi.entity.Image;
import AISL.ADizzi.exception.ApiException;
import AISL.ADizzi.repository.ImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import AISL.ADizzi.exception.ErrorType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private static final String IMAGE_BASE_URL = "http://localhost:8080/images/"; // 이미지 기본 URL
    private static final String IMAGE_UPLOAD_PATH = "/path/to/image/storage/";// 이미지 저장 경로

    private final ImageRepository imageRepository;

    public Image saveImage(MultipartFile file, Long type) {

        if (file.isEmpty()) {
            throw new ApiException(ErrorType.INVALID_IMAGE, "이미지 파일이 비어 있습니다.");
        }

        try {
            // 파일 이름 생성 (UUID 사용)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_UPLOAD_PATH + fileName);

            // 파일 저장
            Files.createDirectories(filePath.getParent()); // 디렉토리 생성
            Files.copy(file.getInputStream(), filePath);

            // Image 엔티티 저장
            String imageUrl = IMAGE_BASE_URL + fileName;
            Image image = new Image(imageUrl, type);
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new ApiException(ErrorType.IMAGE_UPLOAD_FAILED, "이미지 업로드 실패: " + e.getMessage());
        }
    }
}
