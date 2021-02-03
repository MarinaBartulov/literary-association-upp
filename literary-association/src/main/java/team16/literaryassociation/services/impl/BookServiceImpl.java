package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BookDTO;
import team16.literaryassociation.dto.BookDetailsDTO;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.repository.BookRepository;
import team16.literaryassociation.services.interfaces.BookService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = this.bookRepository.findAll();
        List<BookDTO> booksDTO = books.stream().map(b -> new BookDTO(b)).collect(Collectors.toList());
        return booksDTO;
    }

    @Override
    public BookDetailsDTO getBookDetails(Long id) {
        Book book = this.bookRepository.findById(id).orElse(null);
        if(book == null){
            return null;
        }
        return new BookDetailsDTO(book);
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findById(id).orElse(null);
    }
}
