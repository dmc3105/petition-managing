package ru.dmc3105.petitionmanaging.security;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        return isAdmin(principal) || (isPetitionCreator(petition, principal) && isUser(principal));
    }


    public boolean hasAccessTo(@NonNull final UserDetails principal, Long petitionId) {
        final Petition petition = petitionService.getPetitionById(petitionId);
        return hasAccessTo(principal, petition);
    }

    private boolean isUser(UserDetails principal) {
        return principal.getAuthorities().contains(new  SimpleGrantedAuthority("ROLE_USER"));
    }

    private boolean isAdmin(UserDetails principal) {
        return principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean isPetitionCreator(Petition petition, UserDetails principal) {
        final StageEvent event = stageEventService.getStageEventByPetitionAndStage(petition, StageEvent.Stage.CREATED);
        final String creatorUsername = event.getAssignee().getUsername();
        return principal.getUsername().equals(creatorUsername);
    }
}
