package barbarski.pawel.books.repository;

import barbarski.pawel.books.entity.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findAllBy(Pageable pageable);
}
