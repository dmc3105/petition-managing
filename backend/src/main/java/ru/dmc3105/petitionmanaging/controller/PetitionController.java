package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dmc3105.petitionmanaging.dto.CreatePetitionRequestDto;
import ru.dmc3105.petitionmanaging.dto.PetitionResponseDto;
import ru.dmc3105.petitionmanaging.dto.UpdatePetitionRequestDto;
import ru.dmc3105.petitionmanaging.dto.UserResponseDto;
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
    public void createPetition(@RequestBody CreatePetitionRequestDto petitionRequestDto, Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        petitionService.addPetition(petitionRequestDto.reason(),
                petitionRequestDto.description(),
                user);
    }

    @GetMapping
    public List<PetitionResponseDto> getAllPetitionsByCreatorUsername(Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        return petitionService.getAllPetitionsByCreator(user).map(this::getPetitionResponseDto).toList();
    }

    @GetMapping("/{id}")
    public PetitionResponseDto getPetitionById(@PathVariable Long id) {
        return getPetitionResponseDto(petitionService.getPetitionById(id));
    }

    @PatchMapping("/{id}")
    public void updatePetitionById(@PathVariable Long id,
                                   @RequestBody UpdatePetitionRequestDto updatePetitionRequestDto) {
        final Petition petition = petitionService.getPetitionById(id);
        petitionService.updatePetition(petition,
                updatePetitionRequestDto.reason(),
                updatePetitionRequestDto.description());
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
                new UserResponseDto(
                        creator.getId(),
                        creator.getUsername(),
                        creator.getFirstname(),
                        creator.getLastname()
                )
        );
    }
}
