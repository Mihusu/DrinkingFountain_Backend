package aau.project.drinkingfountainbackend.persistence.entity;

import aau.project.drinkingfountainbackend.persistence.projection.ReviewsScoreSumProjection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReviewsScoreSumProjectionMock {
    public static ReviewsScoreSumProjection createMock(int sum, int count) {
        ReviewsScoreSumProjection mockProjection = mock(ReviewsScoreSumProjection.class);
        when(mockProjection.getSum()).thenReturn(sum);
        when(mockProjection.getCount()).thenReturn(count);
        return mockProjection;
    }
}
