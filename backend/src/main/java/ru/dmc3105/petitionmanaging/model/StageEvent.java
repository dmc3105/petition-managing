package ru.dmc3105.petitionmanaging.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stage_events")
public class StageEvent {
    public enum Stage {CREATED, VIEWED, PROCESSING, COMPLETED, CANCELED}

    public static StageEvent createJustHappenedEvent(
            Stage stage,
            User assignee,
            Petition petition
    ) {
        return new StageEvent(
                null,
                new Date(),
                true,
                stage,
                assignee,
                petition
        );
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(updatable = false, nullable = false)
    private Date occurenceDate;

    @Column(nullable = false)
    private Boolean isCurrent;

    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User assignee;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Petition petition;

}
