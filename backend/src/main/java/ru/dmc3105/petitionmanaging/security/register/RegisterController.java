package ru.dmc3105.petitionmanaging.security.register;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegisterController {
    private RegisterService service;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto registerRequestDto) {
        service.register(registerRequestDto.username(),
                registerRequestDto.password(),
                registerRequestDto.firstname(),
                registerRequestDto.lastname());
    }
}
