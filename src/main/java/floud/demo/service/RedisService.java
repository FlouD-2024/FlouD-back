package floud.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String REDIS_KEY_PREFIX = "refresh_token:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = REDIS_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    public String getRefreshToken(Long userId) {
        String key = REDIS_KEY_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

}
