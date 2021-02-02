package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.repository.BookRepository;
import team16.literaryassociation.services.interfaces.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAllWritersBooks(Long writerId) {
        return bookRepository.findAllWritersBooks(writerId);
    }
}
