package AISL.ADizzi.controller;

import AISL.ADizzi.dto.request.CreateContainerRequest;
import AISL.ADizzi.dto.request.UpdateContainerRequest;
import AISL.ADizzi.dto.response.ContainerResponse;
import AISL.ADizzi.service.ContainerService;
import AISL.ADizzi.service.S3ImageService;
import AISL.ADizzi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "이미지 API")
public class ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping("/s3/upload")
    public ResponseEntity<?> s3Upload(@RequestPart(value = "image", required = false) MultipartFile image){
        String profileImage = s3ImageService.upload(image);
        return ResponseEntity.ok(profileImage);
    }

    @GetMapping("/s3/delete")
    public ResponseEntity<?> s3delete(@RequestParam String addr){
        s3ImageService.deleteImageFromS3(addr);
        return ResponseEntity.ok(null);
    }
}
