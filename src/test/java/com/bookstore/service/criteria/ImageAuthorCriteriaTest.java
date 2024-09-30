package com.bookstore.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageAuthorCriteriaTest {

    @Test
    void newImageAuthorCriteriaHasAllFiltersNullTest() {
        var imageAuthorCriteria = new ImageAuthorCriteria();
        assertThat(imageAuthorCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void imageAuthorCriteriaFluentMethodsCreatesFiltersTest() {
        var imageAuthorCriteria = new ImageAuthorCriteria();

        setAllFilters(imageAuthorCriteria);

        assertThat(imageAuthorCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void imageAuthorCriteriaCopyCreatesNullFilterTest() {
        var imageAuthorCriteria = new ImageAuthorCriteria();
        var copy = imageAuthorCriteria.copy();

        assertThat(imageAuthorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(imageAuthorCriteria)
        );
    }

    @Test
    void imageAuthorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageAuthorCriteria = new ImageAuthorCriteria();
        setAllFilters(imageAuthorCriteria);

        var copy = imageAuthorCriteria.copy();

        assertThat(imageAuthorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(imageAuthorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageAuthorCriteria = new ImageAuthorCriteria();

        assertThat(imageAuthorCriteria).hasToString("ImageAuthorCriteria{}");
    }

    private static void setAllFilters(ImageAuthorCriteria imageAuthorCriteria) {
        imageAuthorCriteria.id();
        imageAuthorCriteria.image_url();
        imageAuthorCriteria.author();
        imageAuthorCriteria.distinct();
    }

    private static Condition<ImageAuthorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getImage_url()) &&
                condition.apply(criteria.getAuthor()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageAuthorCriteria> copyFiltersAre(ImageAuthorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getImage_url(), copy.getImage_url()) &&
                condition.apply(criteria.getAuthor(), copy.getAuthor()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
