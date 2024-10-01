package ru.dmc3105.petitionmanaging.security;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

@AllArgsConstructor
@Service
public class AuthorizationService {
    private StageEventService stageEventService;
    private PetitionService petitionService;

    public boolean hasAccessTo(@NonNull final UserDetails principal, Petition petition) {
        StageEvent event = stageEventService.getStageEventByPetitionAndStage(petition, StageEvent.Stage.CREATED);
        return principal.getUsername().equals(event.getAssignee().getUsername());
    }

    public boolean hasAccessTo(@NonNull final UserDetails principal, Long petitionId) {
        final Petition petition = petitionService.getPetitionById(petitionId);
        return hasAccessTo(principal, petition);
    }
}
