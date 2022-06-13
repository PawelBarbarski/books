package barbarski.pawel.books.controller;

import barbarski.pawel.books.repository.model.Book;
import barbarski.pawel.books.service.BookInputException;
import barbarski.pawel.books.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<String> add(@RequestBody Book book) {
        try {
            Integer id = bookService.add(book);
            return ResponseEntity.created(URI.create("/book/" + id)).body(null);
        } catch (BookInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/book/{page}")
    public ResponseEntity<?> getAll(@PathVariable("page") Integer page) {
        if (page < 0) {
            return ResponseEntity.badRequest().body("Page cannot be negative.");
        }
        return ResponseEntity.ok(bookService.get(page));
    }

    @PutMapping("/book")
    public ResponseEntity<String> edit(@RequestBody Book book, @RequestParam("id") Integer id) {
        try {
            bookService.edit(book, id);
            return ResponseEntity.ok().body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (BookInputException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/book")
    public ResponseEntity<String> remove(@RequestParam("id") Integer id) {
        try {
            bookService.remove(id);
            return ResponseEntity.ok().body(null);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
