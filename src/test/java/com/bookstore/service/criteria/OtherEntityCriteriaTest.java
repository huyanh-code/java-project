package com.bookstore.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OtherEntityCriteriaTest {

    @Test
    void newOtherEntityCriteriaHasAllFiltersNullTest() {
        var otherEntityCriteria = new OtherEntityCriteria();
        assertThat(otherEntityCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void otherEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var otherEntityCriteria = new OtherEntityCriteria();

        setAllFilters(otherEntityCriteria);

        assertThat(otherEntityCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void otherEntityCriteriaCopyCreatesNullFilterTest() {
        var otherEntityCriteria = new OtherEntityCriteria();
        var copy = otherEntityCriteria.copy();

        assertThat(otherEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(otherEntityCriteria)
        );
    }

    @Test
    void otherEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var otherEntityCriteria = new OtherEntityCriteria();
        setAllFilters(otherEntityCriteria);

        var copy = otherEntityCriteria.copy();

        assertThat(otherEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(otherEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var otherEntityCriteria = new OtherEntityCriteria();

        assertThat(otherEntityCriteria).hasToString("OtherEntityCriteria{}");
    }

    private static void setAllFilters(OtherEntityCriteria otherEntityCriteria) {
        otherEntityCriteria.id();
        otherEntityCriteria.name();
        otherEntityCriteria.birthDate();
        otherEntityCriteria.distinct();
    }

    private static Condition<OtherEntityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OtherEntityCriteria> copyFiltersAre(OtherEntityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
