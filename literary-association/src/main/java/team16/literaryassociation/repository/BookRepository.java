package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
