package ru.dmc3105.petitionmanaging.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    private String reason;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private User executor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    @JsonBackReference
    private User creator;
}
