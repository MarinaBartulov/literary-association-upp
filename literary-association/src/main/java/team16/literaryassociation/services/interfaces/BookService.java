package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);
    List<Book> findAllWritersBooks(Long writerId);
}
