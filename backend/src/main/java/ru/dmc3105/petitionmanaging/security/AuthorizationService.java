package ru.dmc3105.petitionmanaging.security;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.Stage;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

@AllArgsConstructor
@Service
public class AuthorizationService {
    private StageEventService stageEventService;
    private PetitionRepository petitionRepository;

    public boolean isPrincipalUsernameAndPetitionOwner(@NonNull final UserDetails principal, String username, Petition petition) {
        return isPrincipalUsername(principal, username) && isPetitionOwner(petition, username);
    }

    public boolean isPetitionOwner(Petition petition, User user) {
        return isPetitionOwner(petition, user.getUsername());
    }

    public boolean isPetitionOwner(Long petitionId, User user) {
        return isPetitionOwner(petitionId, user.getUsername());
    }

    public boolean isPetitionOwner(Long petitionId, String username) {
        Petition petition = petitionRepository.findById(petitionId).orElseThrow();
        return isPetitionOwner(petition, username);
    }

    public boolean isPetitionOwner(Petition petition, String username) {
        return stageEventService.getStageEventByPetitionAndStage(petition, Stage.CREATED)
                .getAssignee()
                .getUsername()
                .equals(username);
    }

    public boolean isPrincipalUsername(@NonNull final UserDetails principal, String username) {
        return principal.getUsername().equals(username);
    }
}
