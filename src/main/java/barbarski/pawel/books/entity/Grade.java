package barbarski.pawel.books.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Grade {
    GRADE_1(1),
    GRADE_2(2),
    GRADE_3(3),
    GRADE_4(4),
    GRADE_5(5);

    public static final Map<Integer, Grade> GRADES = Arrays.stream(Grade.values())
            .collect(Collectors.toMap(Grade::getNumber, Function.identity()));

    @Getter
    private final int number;

    public static Optional<Grade> of(int number) {
        return Optional.ofNullable(GRADES.get(number));
    }
}
