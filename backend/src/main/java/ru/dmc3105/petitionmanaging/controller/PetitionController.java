package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dmc3105.petitionmanaging.dto.CreatePetitionRequestDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/petition")
public class PetitionController {
    private PetitionService service;
    @PostMapping
    public void createPetition(@RequestBody CreatePetitionRequestDto petitionRequestDto, Principal principal) {
        service.createPetition(petitionRequestDto.reason(),
                petitionRequestDto.description(),
                principal.getName());
    }

    @GetMapping
    public List<Petition> findAllPetitionsByUsername(Principal principal) {
        return service.findAllPetitionsByCreatorUsername(principal.getName());
    }
}
