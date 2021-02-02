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
    public Book findOne(Long id) {
        return bookRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<Book> findAllWritersBooks(Long writerId) {
        return bookRepository.findAllWritersBooks(writerId);
    }

    @Override
    public Book findBookByWriterAndTitle(Long writerId, String title) {
        return bookRepository.findBookByWriterAndTitle(writerId, title);
    }

    @Override
    public Book findBookByTitleAndWritersName(String title, String writerFirstName, String writerLastName) {
        return bookRepository.findBookByTitleAndWritersNameIgnoreCase(title, writerFirstName, writerLastName);
    }
}
