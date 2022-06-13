package barbarski.pawel.books.service;

import barbarski.pawel.books.entity.BookEntity;
import barbarski.pawel.books.repository.BookRepository;
import barbarski.pawel.books.repository.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Spy
    private final BookMapper bookMapper = new BookMapper();

    @Captor
    private ArgumentCaptor<BookEntity> bookEntityCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private final int id = 123;
    private final String author = "author";
    private final String title = "title";
    private final String isbn = "0000000000000";
    private final int pagesNumber = 100;
    private final int grade = 1;
    private final Book book = Book.builder()
            .id(null)
            .author(author)
            .title(title)
            .isbn(isbn)
            .pagesNumber(pagesNumber)
            .grade(grade)
            .build();

    private final int page = 3;

    @BeforeEach
    void before() {
        bookService = new BookService(bookRepository, bookMapper);
    }

    @Test
    void add() {
        //given
        when(bookRepository.save(any())).thenAnswer(inv -> {
            Object entity = inv.getArguments()[0];
            ((BookEntity) entity).setId(id);
            return entity;
        });

        //when
        Integer returnedId = bookService.add(book);

        //then
        verify(bookMapper).mapToEntity(book);
        assertThat(returnedId).isEqualTo(id);
    }

    @Test
    void editWhenPresent() {
        //given
        when(bookRepository.findById(id)).thenReturn(Optional.of(new BookEntity()));

        //when
        bookService.edit(book, id);

        //then
        verify(bookMapper).mapToEntity(book);
        verify(bookRepository).save(bookEntityCaptor.capture());
        assertThat(bookEntityCaptor.getValue().getId()).isEqualTo(id);
    }

    @Test
    void editWhenAbsent() {
        //given
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> bookService.edit(book, id)).isInstanceOf(NoSuchElementException.class);

        //then
        verify(bookMapper, never()).mapToEntity(book);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void get() {
        //given
        when(bookRepository.findAllBy(any())).thenReturn(List.of(new BookEntity(), new BookEntity()));

        //when
        List<Book> books = bookService.get(page);

        //then
        verify(bookRepository).findAllBy(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page);
        verify(bookMapper, times(2)).mapFromEntity(any());
        assertThat(books).hasSize(2);
    }

    @Test
    void remove() {
        //when
        bookService.remove(id);

        //then
        verify(bookRepository).deleteById(id);
    }
}