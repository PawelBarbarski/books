package barbarski.pawel.books.entity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class GradeTest {

    @ParameterizedTest()
    @EnumSource(Grade.class)
    void of(Grade grade) {
        assertThat(Grade.of(grade.getNumber()).get()).isEqualTo(grade);
    }
}