package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.mapper.PetitionToDtoMapper;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.request.AddPetitionRequest;
import ru.dmc3105.petitionmanaging.request.UpdatePetitionRequest;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user/petition")
public class PetitionUserController {
    private PetitionService petitionService;
    private PetitionToDtoMapper petitionMapper;

    @PostMapping
    public PetitionDto createPetitionByPrincipal(@RequestBody AddPetitionRequest petitionRequestDto, Principal principal) {
        final Petition createdPetition = petitionService.addPetition(petitionRequestDto, principal.getName());
        return petitionMapper.petitionToDto(createdPetition);
    }

    @GetMapping
    public List<PetitionDto> getAllPetitionsByPrincipal(Principal principal) {
        return petitionService.getAllPetitionsByCreatorUsername(principal.getName())
                .map(petitionMapper::petitionToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public PetitionDto getPetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        return petitionMapper.petitionToDto(petition);
    }


    @PutMapping("/{id}/update")
    public PetitionDto updatePetitionById(@PathVariable Long id,
                                                      @RequestBody UpdatePetitionRequest updatePetitionRequestDto) {
        final Petition updatedPetition = petitionService.updatePetition(id, updatePetitionRequestDto);
        return petitionMapper.petitionToDto(updatedPetition);
    }

    @PatchMapping("/{id}/cancel")
    public PetitionDto cancelPetitionById(@PathVariable Long id, Principal principal) {
        final Petition updatedPetition = petitionService.cancelPetitionById(id);
        return petitionMapper.petitionToDto(updatedPetition);
    }
}
