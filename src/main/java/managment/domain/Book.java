package managment.domain;

import com.datastax.driver.mapping.annotations.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A Book.
 */

@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private String title;

    private String author;

    private Integer numberOfBooks;

    @NotNull
    private Boolean availability;

    private LocalDate issuedDate;

    private LocalDate returningDate;

    private String stackNumber;

    private String departmentType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNumberOfBooks() {
        return numberOfBooks;
    }

    public Book numberOfBooks(Integer numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
        return this;
    }

    public void setNumberOfBooks(Integer numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public Boolean isAvailability() {
        return availability;
    }

    public Book availability(Boolean availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public Book issuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
        return this;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getReturningDate() {
        return returningDate;
    }

    public Book returningDate(LocalDate returningDate) {
        this.returningDate = returningDate;
        return this;
    }

    public void setReturningDate(LocalDate returningDate) {
        this.returningDate = returningDate;
    }

    public String getStackNumber() {
        return stackNumber;
    }

    public Book stackNumber(String stackNumber) {
        this.stackNumber = stackNumber;
        return this;
    }

    public void setStackNumber(String stackNumber) {
        this.stackNumber = stackNumber;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public Book departmentType(String departmentType) {
        this.departmentType = departmentType;
        return this;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if(book.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", author='" + author + "'" +
            ", numberOfBooks='" + numberOfBooks + "'" +
            ", availability='" + availability + "'" +
            ", issuedDate='" + issuedDate + "'" +
            ", returningDate='" + returningDate + "'" +
            ", stackNumber='" + stackNumber + "'" +
            ", departmentType='" + departmentType + "'" +
            '}';
    }
}
