package ru.dmc3105.petitionmanaging.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stage_events")
public class StageEvent {
    public enum Stage {CREATED, VIEWED, PROCESSING, COMPLETED, CANCELED}

    @Id
    @GeneratedValue
    private Long id;

    private Date occurenceDate;

    private Boolean isCurrent;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User assignee;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Petition petition;

}
