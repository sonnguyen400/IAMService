package com.sonnguyen.iam.service;

import com.sonnguyen.iam.model.ForbiddenToken;
import com.sonnguyen.iam.repository.ForbiddenTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ForbiddenTokenService {
    ForbiddenTokenRepository forbiddenTokenRepository;

    public boolean existsByToken(String token) {
        return forbiddenTokenRepository.existsByToken(token);
    }

    public void save(String token) {
        ForbiddenToken forbiddenToken = new ForbiddenToken();
        forbiddenToken.setToken(token);
        forbiddenTokenRepository.save(forbiddenToken);
    }
    public void saveAll(String ...token) {
        List<ForbiddenToken> forbiddenTokens = Arrays.stream(token).map(ForbiddenToken::new).toList();
        forbiddenTokenRepository.saveAll(forbiddenTokens);
    }
}
