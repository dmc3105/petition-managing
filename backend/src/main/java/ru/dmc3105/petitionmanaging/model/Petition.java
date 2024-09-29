package ru.dmc3105.petitionmanaging.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "petitions")
public class Petition {

    @Id
    @GeneratedValue
    private Long id;

    private String reason;

    private String description;

    @OneToMany
    @JoinColumn(name = "petition_id")
    private List<StageEvent> events;
}
