package aau.project.drinkingfountainbackend.persistence.projection;

import java.util.Optional;

public interface ReviewsScoreSumProjection {
    Optional<Integer> getSum();
    Optional<Integer> getCount();
}