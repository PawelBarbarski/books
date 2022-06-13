package barbarski.pawel.books.controller;

import barbarski.pawel.books.BooksApplication;
import barbarski.pawel.books.repository.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BooksApplication.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String isbn0 = "0000000000000";
    String isbn1 = "9000000000001";
    String isbn2 = "8000000000002";
    String isbn3 = "7000000000003";

    private final Book book0 = Book.builder()
            .id(null)
            .author("author0")
            .title("title0")
            .isbn(isbn0)
            .pagesNumber(100)
            .grade(1)
            .build();
    private final Book book1 = Book.builder()
            .id(null)
            .author("author1")
            .title("title1")
            .isbn(isbn1)
            .pagesNumber(101)
            .grade(2)
            .build();
    private final Book book2 = Book.builder()
            .id(null)
            .author("author2")
            .title("title2")
            .isbn(isbn2)
            .pagesNumber(102)
            .grade(3)
            .build();
    private final Book wrongBook = Book.builder()
            .id(null)
            .author(null)
            .title(null)
            .isbn(null)
            .pagesNumber(null)
            .grade(null)
            .build();
    private final Book bookModified = Book.builder()
            .id(null)
            .author("author0")
            .title("title0")
            .isbn(isbn3)
            .pagesNumber(100)
            .grade(1)
            .build();

    @Test
    void endToEnd() throws Exception {
        addBooks();
        tryToAddWrongBook();
        getAllBooks();
        tryToGetWrongPage();
        modifyBook();
        tryToModifyWithWrongBook();
        tryToModifyNonExistingBook();
        deleteBook();
        tryToDeleteNonExistingBook();
        getAllBooksAfterChanges();
    }

    private void addBooks() throws Exception {
        mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book0)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/book/1"));
        mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/book/2"));
        mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book2)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/book/3"));
    }

    private void tryToAddWrongBook() throws Exception {
        mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongBook)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    private void getAllBooks() throws Exception {
        mvc.perform(get("/book/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isbn").value(isbn0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].isbn").value(isbn1));
        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].isbn").value(isbn2));
        mvc.perform(get("/book/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private void tryToGetWrongPage() throws Exception {
        mvc.perform(get("/book/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    private void modifyBook() throws Exception {
        mvc.perform(put("/book?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookModified)))
                .andExpect(status().isOk());
    }

    private void tryToModifyWithWrongBook() throws Exception {
        mvc.perform(put("/book?id=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongBook)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    private void tryToModifyNonExistingBook() throws Exception {
        mvc.perform(put("/book?id=4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongBook)))
                .andExpect(status().isNotFound());
    }

    private void deleteBook() throws Exception {
        mvc.perform(delete("/book?id=2"))
                .andExpect(status().isOk());
    }

    private void tryToDeleteNonExistingBook() throws Exception {
        mvc.perform(delete("/book?id=4"))
                .andExpect(status().isNotFound());
    }

    private void getAllBooksAfterChanges() throws Exception {
        mvc.perform(get("/book/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isbn").value(isbn3))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].isbn").value(isbn2));
        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}