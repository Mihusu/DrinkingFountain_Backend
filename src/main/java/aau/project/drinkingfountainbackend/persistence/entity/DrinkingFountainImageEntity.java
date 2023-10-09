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
@Table(name = "fountain_images")
public class DrinkingFountainImageEntity {
    @Id
    @Column(name = "fountain_images_id")
    private int id;
    private byte[] image;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="drinking_fountain_id", nullable=false)
    private DrinkingFountainEntity drinkingFountainEntity;
}
