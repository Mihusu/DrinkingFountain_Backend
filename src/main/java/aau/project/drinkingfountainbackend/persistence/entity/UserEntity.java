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
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    private String name;
    private String password;
    @Column(name = "role")
    private RoleType role;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    public enum RoleType {
        USER("USER"), ADMIN("AMID");

        RoleType(String name) {
        }
    }
}
