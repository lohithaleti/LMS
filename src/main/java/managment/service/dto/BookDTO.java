package managment.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the Book entity.
 */
public class BookDTO implements Serializable {

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

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public Integer getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(Integer numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }
    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }
    public LocalDate getReturningDate() {
        return returningDate;
    }

    public void setReturningDate(LocalDate returningDate) {
        this.returningDate = returningDate;
    }
    public String getStackNumber() {
        return stackNumber;
    }

    public void setStackNumber(String stackNumber) {
        this.stackNumber = stackNumber;
    }
    public String getDepartmentType() {
        return departmentType;
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

        BookDTO bookDTO = (BookDTO) o;

        if ( ! Objects.equals(id, bookDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookDTO{" +
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
