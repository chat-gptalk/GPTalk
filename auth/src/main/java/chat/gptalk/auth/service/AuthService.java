package chat.gptalk.auth.service;


import static chat.gptalk.security.SecurityConstants.ACCESS_TOKEN_DURATION;
import static chat.gptalk.security.SecurityConstants.REFRESH_TOKEN_DURATION;

import chat.gptalk.auth.config.AuthProperties;
import chat.gptalk.auth.model.request.LoginRequest;
import chat.gptalk.auth.model.request.RegisterRequest;
import chat.gptalk.auth.model.response.LoginResponse;
import chat.gptalk.auth.model.response.RegisterResponse;
import chat.gptalk.auth.repository.TenantRepository;
import chat.gptalk.auth.repository.UserRepository;
import chat.gptalk.auth.util.JwtUtils;
import chat.gptalk.common.constants.EntityStatus;
import chat.gptalk.common.constants.ErrorCode;
import chat.gptalk.common.entity.TenantEntity;
import chat.gptalk.common.entity.UserEntity;
import chat.gptalk.common.exception.BizException;
import chat.gptalk.common.util.BeanMapperUtils;
import chat.gptalk.security.SecurityConstants;
import chat.gptalk.security.model.AdminUser;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository repository,
        TenantRepository tenantRepository,
        PasswordEncoder passwordEncoder,
        AuthProperties authProperties) {
        this.repository = repository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = new JwtUtils(authProperties.jwt().publicKey(), authProperties.jwt().privateKey());
    }

    public LoginResponse login(LoginRequest request) {
        UserEntity userEntity = repository.findOneByUsername(request.username());
        if (userEntity == null ||
            !passwordEncoder.matches(request.password(), userEntity.password())) {
            throw new BizException(ErrorCode.AU_INVALID_CREDENTIALS);
        }
        AdminUser adminUser = new AdminUser(userEntity.userId(),
            userEntity.username(),
            userEntity.tenantId(),
            userEntity.roles());
        String accessToken = jwtUtils.generateToken(adminUser, ACCESS_TOKEN_DURATION);
        String refreshToken = jwtUtils.generateToken(adminUser, REFRESH_TOKEN_DURATION);
        return new LoginResponse(accessToken, refreshToken, ACCESS_TOKEN_DURATION.getSeconds(), "Bearer");
    }

    public RegisterResponse register(RegisterRequest request) {
        UserEntity userEntity = repository.findOneByUsername(request.username());
        if (userEntity != null) {
            throw new BizException(ErrorCode.AU_DUPLICATE_USER, request.username());
        }
        UUID userId = UUID.randomUUID();
        TenantEntity tenantEntity = TenantEntity.builder()
            .tenantId(UUID.randomUUID())
            .name(request.username())
            .ownerUserId(userId)
            .status(EntityStatus.ACTIVE.getCode())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        tenantRepository.save(tenantEntity);
        UserEntity user = UserEntity.builder()
            .userId(userId)
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .status(EntityStatus.ACTIVE.getCode())
            .tenantId(tenantEntity.tenantId())
            .roles(List.of(SecurityConstants.ROLE_ADMIN))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
        UserEntity saved = repository.save(user);
        return BeanMapperUtils.map(saved, RegisterResponse.class);
    }
}
