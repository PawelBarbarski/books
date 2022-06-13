package barbarski.pawel.books.service;

import barbarski.pawel.books.entity.BookEntity;
import barbarski.pawel.books.entity.Grade;
import barbarski.pawel.books.repository.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookEntity mapToEntity(Book book) {
        BookEntity bookEntity = new BookEntity();

        String author = book.getAuthor();
        validateAuthor(author);
        bookEntity.setAuthor(author);

        String title = book.getTitle();
        validateTitle(title);
        bookEntity.setTitle(title);

        Integer pagesNumber = book.getPagesNumber();
        validatePagesNumber(pagesNumber);
        bookEntity.setPagesNumber(pagesNumber);

        String isbn = book.getIsbn();
        validateIsbn(isbn);
        bookEntity.setIsbn(isbn);

        Integer gradeNumber = book.getGrade();
        Grade grade = findGradeWithValidation(gradeNumber);
        bookEntity.setGrade(grade);

        return bookEntity;
    }

    private void validateAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new BookInputException("Author should not be blank.");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BookInputException("Title should not be blank.");
        }
    }

    private Grade findGradeWithValidation(Integer gradeNumber) {
        return Grade.of(gradeNumber).orElseThrow(() -> {
            throw new BookInputException("Grade should be one of the allowed values: " + Grade.GRADES.keySet());
        });
    }

    private void validatePagesNumber(Integer pagesNumber) {
        if (pagesNumber <= 0) {
            throw new BookInputException("Number of pages should be positive.");
        }
    }

    private void validateIsbn(String isbn) {
        char[] digits = isbn.toCharArray();
        if (digits.length != 13) {
            throw new BookInputException("ISBN should be of length 13.");
        }
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int numericValue = Character.digit(digits[i], 10);
            if (numericValue < 0) {
                throw new BookInputException("ISBN should consist only of digits.");
            }
            sum = sum + numericValue * ((i % 2 == 0) ? 1 : 3);
        }
        if (sum % 10 != 0) {
            throw new BookInputException("ISBN should have the proper control sum.");
        }
    }

    public Book mapFromEntity(BookEntity bookEntity) {
        Grade grade = bookEntity.getGrade();
        return Book.builder()
                .id(bookEntity.getId())
                .author(bookEntity.getAuthor())
                .title(bookEntity.getTitle())
                .pagesNumber(bookEntity.getPagesNumber())
                .isbn(bookEntity.getIsbn())
                .grade(grade != null ? grade.getNumber() : null)
                .build();
    }

}
