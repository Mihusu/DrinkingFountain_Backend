package aau.project.drinkingfountainbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Data // Generates getters and setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "drinking_fountain")
public class DrinkingFountainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drinking_fountain_id")
    private int id;
    private double latitude;
    private double longitude;
    @Enumerated(EnumType.STRING)
    @Column(name = "fountain_type")
    private FountainType type;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    private boolean approved;
    private double score;

    public enum FountainType {
        FILLING, DRINKING
    }
}


