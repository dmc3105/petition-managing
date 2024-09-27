package ru.dmc3105.petitionmanaging.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PetitionStage {
    public enum Stage {CREATED, VIEWED, PROCESSING, COMPLETED, CANCELED}

    @Id
    @GeneratedValue
    private Long id;

    private Date stage_creation_date;

    private Boolean isCurrent;

    @ManyToOne
    private User assignee;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @ManyToOne
    private Petition petition;

}
