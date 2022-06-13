package barbarski.pawel.books.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Book {
    Integer id;
    String title;
    String author;
    String isbn;
    Integer pagesNumber;
    Integer grade;
}
