package managment.web.rest;

import managment.AbstractCassandraTest;
import managment.LibraryApp;

import managment.domain.Book;
import managment.repository.BookRepository;
import managment.service.BookService;
import managment.service.dto.BookDTO;
import managment.service.mapper.BookMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibraryApp.class)
public class BookResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_BOOKS = 1;
    private static final Integer UPDATED_NUMBER_OF_BOOKS = 2;

    private static final Boolean DEFAULT_AVAILABILITY = false;
    private static final Boolean UPDATED_AVAILABILITY = true;

    private static final LocalDate DEFAULT_ISSUED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RETURNING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURNING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STACK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_STACK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_TYPE = "BBBBBBBBBB";

    @Inject
    private BookRepository bookRepository;

    @Inject
    private BookMapper bookMapper;

    @Inject
    private BookService bookService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookMockMvc;

    private Book book;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource();
        ReflectionTestUtils.setField(bookResource, "bookService", bookService);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity() {
        Book book = new Book()
                .title(DEFAULT_TITLE)
                .author(DEFAULT_AUTHOR)
                .numberOfBooks(DEFAULT_NUMBER_OF_BOOKS)
                .availability(DEFAULT_AVAILABILITY)
                .issuedDate(DEFAULT_ISSUED_DATE)
                .returningDate(DEFAULT_RETURNING_DATE)
                .stackNumber(DEFAULT_STACK_NUMBER)
                .departmentType(DEFAULT_DEPARTMENT_TYPE);
        return book;
    }

    @Before
    public void initTest() {
        bookRepository.deleteAll();
        book = createEntity();
    }

    @Test
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
                .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBook.getNumberOfBooks()).isEqualTo(DEFAULT_NUMBER_OF_BOOKS);
        assertThat(testBook.isAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testBook.getIssuedDate()).isEqualTo(DEFAULT_ISSUED_DATE);
        assertThat(testBook.getReturningDate()).isEqualTo(DEFAULT_RETURNING_DATE);
        assertThat(testBook.getStackNumber()).isEqualTo(DEFAULT_STACK_NUMBER);
        assertThat(testBook.getDepartmentType()).isEqualTo(DEFAULT_DEPARTMENT_TYPE);
    }

    @Test
    public void checkAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setAvailability(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
                .andExpect(status().isBadRequest());

        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get all the books
        restBookMockMvc.perform(get("/api/books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].numberOfBooks").value(hasItem(DEFAULT_NUMBER_OF_BOOKS)))
                .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.booleanValue())))
                .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
                .andExpect(jsonPath("$.[*].returningDate").value(hasItem(DEFAULT_RETURNING_DATE.toString())))
                .andExpect(jsonPath("$.[*].stackNumber").value(hasItem(DEFAULT_STACK_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].departmentType").value(hasItem(DEFAULT_DEPARTMENT_TYPE.toString())));
    }

    @Test
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.numberOfBooks").value(DEFAULT_NUMBER_OF_BOOKS))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.booleanValue()))
            .andExpect(jsonPath("$.issuedDate").value(DEFAULT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.returningDate").value(DEFAULT_RETURNING_DATE.toString()))
            .andExpect(jsonPath("$.stackNumber").value(DEFAULT_STACK_NUMBER.toString()))
            .andExpect(jsonPath("$.departmentType").value(DEFAULT_DEPARTMENT_TYPE.toString()));
    }

    @Test
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findOne(book.getId());
        updatedBook
                .title(UPDATED_TITLE)
                .author(UPDATED_AUTHOR)
                .numberOfBooks(UPDATED_NUMBER_OF_BOOKS)
                .availability(UPDATED_AVAILABILITY)
                .issuedDate(UPDATED_ISSUED_DATE)
                .returningDate(UPDATED_RETURNING_DATE)
                .stackNumber(UPDATED_STACK_NUMBER)
                .departmentType(UPDATED_DEPARTMENT_TYPE);
        BookDTO bookDTO = bookMapper.bookToBookDTO(updatedBook);

        restBookMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
                .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeUpdate);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBook.getNumberOfBooks()).isEqualTo(UPDATED_NUMBER_OF_BOOKS);
        assertThat(testBook.isAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testBook.getIssuedDate()).isEqualTo(UPDATED_ISSUED_DATE);
        assertThat(testBook.getReturningDate()).isEqualTo(UPDATED_RETURNING_DATE);
        assertThat(testBook.getStackNumber()).isEqualTo(UPDATED_STACK_NUMBER);
        assertThat(testBook.getDepartmentType()).isEqualTo(UPDATED_DEPARTMENT_TYPE);
    }

    @Test
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeDelete - 1);
    }
}
