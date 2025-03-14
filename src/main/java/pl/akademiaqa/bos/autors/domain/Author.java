package pl.akademiaqa.bos.autors.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.akademiaqa.bos.books.domain.Book;
import pl.akademiaqa.bos.jpa.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "books")
@Table(name = "authors")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Author extends BaseEntity implements Comparable<Author> {
    @Column(length = 255, nullable = false)
    String firstName;
    @Column(length = 255, nullable = false)
    String lastName;
    @JsonIgnore
    @Column(length = 512, nullable = false)
    String fullName;

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("authors")
    @JsonIgnore
    Set<Book> books = new HashSet<>();

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedAt;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public int compareTo(Author o) {
        return this.fullName.compareTo(o.getFullName());
    }
}
