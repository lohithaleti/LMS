package managment.repository;

import managment.domain.Book;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Book entity.
 */
@Repository
public class BookRepository {

    @Inject
    private Session session;

    private Mapper<Book> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Book.class);
        findAllStmt = session.prepare("SELECT * FROM book");
        truncateStmt = session.prepare("TRUNCATE book");
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Book book = new Book();
                book.setId(row.getUUID("id"));
                book.setTitle(row.getString("title"));
                book.setAuthor(row.getString("author"));
                book.setNumberOfBooks(row.getInt("numberOfBooks"));
                book.setAvailability(row.getBool("availability"));
                book.setIssuedDate(row.get("issuedDate", LocalDate.class));
                book.setReturningDate(row.get("returningDate", LocalDate.class));
                book.setStackNumber(row.getString("stackNumber"));
                book.setDepartmentType(row.getString("departmentType"));
                return book;
            }
        ).forEach(books::add);
        return books;
    }

    public Book findOne(UUID id) {
        return mapper.get(id);
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID());
        }
        mapper.save(book);
        return book;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
