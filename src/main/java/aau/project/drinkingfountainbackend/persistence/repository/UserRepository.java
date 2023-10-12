package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer>  {

}
