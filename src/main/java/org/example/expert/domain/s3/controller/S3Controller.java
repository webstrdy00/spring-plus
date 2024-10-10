package org.example.expert.domain.s3.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.s3.service.S3Service;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    /**
     * 이미지 업로드 api
     * @param file
     * @return 이미지 url
     */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart("file")MultipartFile file){
        try {
            String imageUrl = s3Service.uploadImage(authUser.getId(), file);
            return ResponseEntity.ok(imageUrl);
        }catch (IOException e){
            return ResponseEntity.badRequest().body("이미지 업로드에 실패했습니다: "+e.getMessage());
        }
    }

    /**
     * 이미지 삭제 api
     * @param authUser
     * @return 메시지
     */
    @DeleteMapping("/images")
    public ResponseEntity<String> deleteImage(@AuthenticationPrincipal AuthUser authUser){
        try {
            s3Service.deleteImage(authUser.getId());
            return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("이미지 삭제에 실패했습니다."+e.getMessage());
        }
    }

    /**
     * 이미지 url 조회 api
     * @param authUser
     * @return 이미지 url
     */
    @GetMapping("/images")
    public ResponseEntity<String> getImageUrl(@AuthenticationPrincipal AuthUser authUser){
        String imageUrl = s3Service.getImageUrl(authUser.getId());
        if (imageUrl != null){
            return ResponseEntity.ok(imageUrl);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
