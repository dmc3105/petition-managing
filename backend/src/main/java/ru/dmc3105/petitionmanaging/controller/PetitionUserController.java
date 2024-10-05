package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.controller.request.AddPetitionRequest;
import ru.dmc3105.petitionmanaging.controller.request.UpdatePetitionRequest;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.UserService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user/petition")
public class PetitionUserController {
    private PetitionService petitionService;
    private UserService userService;
    private PetitionToDtoMapper petitionMapper;

    @PostMapping
    public PetitionDto createPetitionByPrincipal(@RequestBody AddPetitionRequest petitionRequestDto,
                                                 Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        final Petition createdPetition = petitionService.addPetition(petitionRequestDto.reason(),
                petitionRequestDto.description(),
                user);
        return petitionMapper.petitionToDto(createdPetition);
    }

    @GetMapping
    public List<PetitionDto> getAllPetitionsByPrincipal(Principal principal) {
        final User user = userService.getUserByUsername(principal.getName());
        return petitionService.getAllPetitionsByCreator(user)
                .map(this::toPetitionResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public PetitionDto getPetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        return toPetitionResponseDto(petition);
    }


    @PutMapping("/{id}")
    public PetitionDto updatePetitionById(@PathVariable Long id,
                                          @RequestBody UpdatePetitionRequest updatePetitionRequestDto) {
        final Petition petition = petitionService.getPetitionById(id);
        final Petition updatedPetition = petitionService.updatePetition(petition,
                updatePetitionRequestDto.reason(),
                updatePetitionRequestDto.description());

        return petitionMapper.petitionToDto(updatedPetition);
    }

    private PetitionDto toPetitionResponseDto(Petition petition) {
        final StageEvent currentEvent = petitionService.getPetitionCurrentStageEvent(petition);
        return petitionMapper.petitionToDto(petition);
    }
}
