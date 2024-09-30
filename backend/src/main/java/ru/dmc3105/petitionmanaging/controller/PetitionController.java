package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dmc3105.petitionmanaging.dto.*;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.UserService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/petition")
public class PetitionController {
    private PetitionService petitionService;
    private UserService userService;
    @PostMapping
    public PetitionInfoResponseDto createPetitionByPrincipal(@RequestBody CreatePetitionRequestDto petitionRequestDto, Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        Petition createdPetition = petitionService.addPetition(petitionRequestDto.reason(),
                petitionRequestDto.description(),
                user);
        return toPetitionInfoResponseDto(createdPetition);
    }


    @GetMapping
    public List<PetitionResponseDto> getAllPetitionsByPrincipal(Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        return petitionService.getAllPetitionsByCreator(user).map(this::getPetitionResponseDto).toList();
    }

    @GetMapping("/{id}")
    public PetitionResponseDto getPetitionById(@PathVariable Long id) {
        return getPetitionResponseDto(petitionService.getPetitionById(id));
    }

    @PutMapping("/{id}")
    public PetitionInfoResponseDto updatePetitionById(@PathVariable Long id,
                                   @RequestBody UpdatePetitionRequestDto updatePetitionRequestDto) {
        final Petition petition = petitionService.getPetitionById(id);
        Petition updatedPetition = petitionService.updatePetition(petition,
                updatePetitionRequestDto.reason(),
                updatePetitionRequestDto.description());

        return toPetitionInfoResponseDto(updatedPetition);
    }

    @DeleteMapping("/{id}")
    public void deletePetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        petitionService.deletePetition(petition);
    }

    private PetitionResponseDto getPetitionResponseDto(Petition petition) {
        final StageEvent currentStageEvent = petitionService.getPetitionCurrentStageEvent(petition);
        final User creator = petitionService.getPetitionCreator(petition);

        return new PetitionResponseDto(
                petition.getId(),
                petition.getReason(),
                petition.getDescription(),
                currentStageEvent.getStage(),
                currentStageEvent.getOccurenceDate(),
                new UserInfoResponseDto(
                        creator.getId(),
                        creator.getUsername(),
                        creator.getFirstname(),
                        creator.getLastname()
                )
        );
    }

    private PetitionInfoResponseDto toPetitionInfoResponseDto(Petition petition) {
        return new PetitionInfoResponseDto(
                petition.getId(),
                petition.getReason(),
                petition.getDescription()
        );
    }
}
