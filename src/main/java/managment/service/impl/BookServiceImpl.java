package managment.service.impl;

import managment.service.BookService;
import managment.domain.Book;
import managment.repository.BookRepository;
import managment.service.dto.BookDTO;
import managment.service.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Book.
 */
@Service
public class BookServiceImpl implements BookService{

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    
    @Inject
    private BookRepository bookRepository;

    @Inject
    private BookMapper bookMapper;

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save
     * @return the persisted entity
     */
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.bookDTOToBook(bookDTO);
        book = bookRepository.save(book);
        BookDTO result = bookMapper.bookToBookDTO(book);
        return result;
    }

    /**
     *  Get all the books.
     *  
     *  @return the list of entities
     */
    public List<BookDTO> findAll() {
        log.debug("Request to get all Books");
        List<BookDTO> result = bookRepository.findAll().stream()
            .map(bookMapper::bookToBookDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one book by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public BookDTO findOne(String id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(UUID.fromString(id));
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        return bookDTO;
    }

    /**
     *  Delete the  book by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(UUID.fromString(id));
    }
}
