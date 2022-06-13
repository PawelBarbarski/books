package barbarski.pawel.books.service;

import barbarski.pawel.books.entity.BookEntity;
import barbarski.pawel.books.repository.BookRepository;
import barbarski.pawel.books.repository.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final int SIZE = 2;

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public Integer add(Book book) {
        BookEntity bookEntity = bookMapper.mapToEntity(book);
        BookEntity savedEntity = bookRepository.save(bookEntity);
        return savedEntity.getId();
    }

    @Transactional
    public void edit(Book book, Integer id) {
        bookRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.format("There is no book with id=%s currently.", id)));
        BookEntity newBookEntity = bookMapper.mapToEntity(book);
        newBookEntity.setId(id);
        bookRepository.save(newBookEntity);
    }

    public List<Book> get(Integer page) {
        return bookRepository.findAllBy(PageRequest.of(page, SIZE)).stream().map(bookMapper::mapFromEntity).toList();
    }

    public void remove(Integer id) {
        bookRepository.deleteById(id);
    }
}
