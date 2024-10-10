package org.example.expert.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    public String uploadImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 기존 이미지가 있다면 삭제
        if (user.getProfileImageUrl() != null){
            deleteImage(userId);
        }

        // 고유한 파일명 생성
        String fileName = createFileName(userId, file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 S3 URL 변환
        String imageUrl =  amazonS3Client.getUrl(bucket, fileName).toString();
        user.updateProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    public void deleteImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImageUrl() != null){
            String fileName = extractFileNameFromUrl(user.getProfileImageUrl());
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            user.updateProfileImageUrl(null);
            userRepository.save(user);
        }
    }
    
    @Transactional(readOnly = true)
    public String getImageUrl(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProfileImageUrl();
    }

    // 고유한 파일명 생성 메서드
    private String createFileName(Long userId, String originalFilename) {
        return "profile-" + userId + "-" + UUID.randomUUID().toString() + getFileExtension(originalFilename);
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("잘못될 형식의 파일 (%s) 입니다.", fileName));
        }
    }
    
    // url에서 파일 이름 추출 메서드
    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }
}
