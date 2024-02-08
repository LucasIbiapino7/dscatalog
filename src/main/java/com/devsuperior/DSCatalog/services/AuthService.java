package com.devsuperior.DSCatalog.services;

import com.devsuperior.DSCatalog.dto.EmailDTO;
import com.devsuperior.DSCatalog.dto.NewPasswordDto;
import com.devsuperior.DSCatalog.entities.PasswordRecover;
import com.devsuperior.DSCatalog.entities.User;
import com.devsuperior.DSCatalog.repositories.PasswordRecoverRepository;
import com.devsuperior.DSCatalog.repositories.UserRepository;
import com.devsuperior.DSCatalog.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailService service;

    @Autowired
    private PasswordRecoverRepository recoverRepository;

    public void createRecoverToken(EmailDTO body) {
        User user = repository.findByEmail(body.getEmail());
        if (user == null){
            throw new ResourceNotFoundException("Email Não Encontrado");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);//gerando um token aleatório
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = recoverRepository.save(entity);
        String text = "Acesse o Link para definir sua nova senha\n\n" + recoverUri + token;
        service.sendEmail(body.getEmail(), "Recuperação de Senha", text);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDto body) {
        List<PasswordRecover> result = recoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if (result.isEmpty()){
            throw new ResourceNotFoundException("Token Inválido");
        }
        User user = repository.findByEmail(result.getFirst().getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user = repository.save(user);
    }
}
