package barbarski.pawel.books.service;

import barbarski.pawel.books.entity.BookEntity;
import barbarski.pawel.books.entity.Grade;
import barbarski.pawel.books.repository.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BookMapperTest {

    private final BookMapper bookMapper = new BookMapper();

    @Test
    void mapToEntity() {
        int id = 123;
        String author = "author";
        String title = "title";
        String isbn = "0000000000000";
        int pagesNumber = 100;
        int grade = 1;
        Book book = Book.builder()
                .id(id)
                .author(author)
                .title(title)
                .isbn(isbn)
                .pagesNumber(pagesNumber)
                .grade(grade)
                .build();

        BookEntity bookEntity = bookMapper.mapToEntity(book);

        assertThat(bookEntity.getId()).isNull();
        assertThat(bookEntity.getAuthor()).isEqualTo(author);
        assertThat(bookEntity.getTitle()).isEqualTo(title);
        assertThat(bookEntity.getIsbn()).isEqualTo(isbn);
        assertThat(bookEntity.getPagesNumber()).isEqualTo(pagesNumber);
        assertThat(bookEntity.getGrade().getNumber()).isEqualTo(grade);
    }

    @ParameterizedTest
    @MethodSource("wrongBooks")
    void throwWhenAuthorNull(Book book) {
        assertThatExceptionOfType(BookInputException.class).isThrownBy(() -> bookMapper.mapToEntity(book));
    }

    private static Stream<Arguments> wrongBooks() {
        return Stream.of(
                Arguments.of(Book.builder()
                        .id(123)
                        .author(null)
                        .title("title")
                        .isbn("0000000000000")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("   ")
                        .title("title")
                        .isbn("0000000000000")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title(null)
                        .isbn("0000000000000")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("    ")
                        .isbn("0000000000000")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("00000000000000")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("000000000000a")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("0000000000001")
                        .pagesNumber(100)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("00000000000000")
                        .pagesNumber(-1)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("00000000000000")
                        .pagesNumber(0)
                        .grade(1)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("00000000000000")
                        .pagesNumber(100)
                        .grade(0)
                        .build()),
                Arguments.of(Book.builder()
                        .id(123)
                        .author("author")
                        .title("title")
                        .isbn("00000000000000")
                        .pagesNumber(100)
                        .grade(6)
                        .build())
        );
    }

    @Test
    void mapFromEntity() {
        int id = 123;
        String author = "author";
        String title = "title";
        String isbn = "0000000000000";
        int pagesNumber = 100;
        Grade grade = Grade.GRADE_1;

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(id);
        bookEntity.setAuthor(author);
        bookEntity.setTitle(title);
        bookEntity.setIsbn(isbn);
        bookEntity.setPagesNumber(pagesNumber);
        bookEntity.setGrade(grade);

        Book book = bookMapper.mapFromEntity(bookEntity);

        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getIsbn()).isEqualTo(isbn);
        assertThat(book.getPagesNumber()).isEqualTo(pagesNumber);
        assertThat(book.getGrade()).isEqualTo(grade.getNumber());
    }

    @Test
    void mapFromEntityWithNullGrade() {
        int id = 123;
        String author = "author";
        String title = "title";
        String isbn = "0000000000000";
        int pagesNumber = 100;

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(id);
        bookEntity.setAuthor(author);
        bookEntity.setTitle(title);
        bookEntity.setIsbn(isbn);
        bookEntity.setPagesNumber(pagesNumber);

        Book book = bookMapper.mapFromEntity(bookEntity);

        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getIsbn()).isEqualTo(isbn);
        assertThat(book.getPagesNumber()).isEqualTo(pagesNumber);
        assertThat(book.getGrade()).isEqualTo(null);
    }
}