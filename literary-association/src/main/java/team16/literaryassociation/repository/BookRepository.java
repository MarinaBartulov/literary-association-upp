package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team16.literaryassociation.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select * from book b where b.writer_id = ?1", nativeQuery = true)
    List<Book> findAllWritersBooks(Long writerId);

}
