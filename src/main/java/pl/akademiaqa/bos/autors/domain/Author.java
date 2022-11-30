package pl.akademiaqa.bos.books.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "authors")
@EntityListeners(AuditingEntityListener.class)
public class Author {

    @Id
    @GeneratedValue
    Long id;
    String firstName;
    String lastName;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authors")
    Set<Book> books;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
