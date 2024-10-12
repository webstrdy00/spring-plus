package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_KEY_PREFIX = "user:nickname:";
    private static final long CACHE_TTL_SECONDS = 3600; // 1 hour

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    public Optional<UserResponse> searchUserByNickname(String nickname) {
        // Redis에서 캐시된 사용자 정보 조회
        String cacheKey = USER_CACHE_KEY_PREFIX + nickname;
        log.info("Attempting to retrieve user from Redis cache. Key: {}", cacheKey);
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue instanceof UserResponse) {
            log.info("User found in Redis cache. Nickname: {}", nickname);
            return Optional.of((UserResponse) cachedValue);
        }

        log.info("User not found in Redis cache. Querying database. Nickname: {}", nickname);

        // 캐시가 없는 경우 DB에서 조회
        return userRepository.findByNickname(nickname)
                .map(user -> {
                    UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getNickname());
                    // 조회 결과를 Redis에 캐시
                    redisTemplate.opsForValue().set(cacheKey, userResponse, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
                    return userResponse;
                });
    }


    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }


}
