package aau.project.drinkingfountainbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data // Generates getters and setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int id;
    private String text;
    private int stars;

    @Enumerated(EnumType.STRING)
    @Column(name = "fountain_type")
    private DrinkingFountainEntity.FountainType type;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "drinking_fountain_id", nullable = false)
    private DrinkingFountainEntity drinkingFountain;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;
}
