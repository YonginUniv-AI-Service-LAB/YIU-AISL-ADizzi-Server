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
@Tag(name = "이미지 관련 API")
public class ImageController {

    private final S3ImageService s3ImageService;

    @Operation(summary = "이미지 업로드. Header: {Content-Type: multipart/form-data}, Body(form-data): {image: IMAGE_FILE}")
    @PostMapping("/s3")
    public ResponseEntity<?> s3Upload(@RequestPart(value = "image", required = false) MultipartFile image){
        String profileImage = s3ImageService.upload(image);
        return ResponseEntity.ok(profileImage);
    }
    @Operation(summary = "이미지 삭제. Params: {addr: IMAGE_URL}")
    @DeleteMapping("/s3")
    public ResponseEntity<?> s3delete(@RequestParam String addr){
        s3ImageService.deleteImageFromS3(addr);
        return ResponseEntity.ok(null);
    }
}
