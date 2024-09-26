package ru.dmc3105.petitionmanaging.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "petitions")
public class Petition {
    public enum Stage {CREATED, VIEWED, PROCESSED, COMPLETED, CANCELED};

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    private String reason;

    private String description;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
}
