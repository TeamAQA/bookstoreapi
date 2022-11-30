package pl.akademiaqa.bos.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "uuid")
public abstract class BaseEntity {

    @Id
    @GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pl.akademiaqa.bos.commons.UseExistingIdOtherwiseGenerateUsingIdentity")
    @GeneratedValue(generator = "UseExistingIdOtherwiseGenerateUsingIdentity")
    @Column(unique = true, nullable = false)
    protected Long id;

    @JsonIgnore
    protected String uuid = UUID.randomUUID().toString();

    @Version
    @JsonIgnore
    private long version;
}
