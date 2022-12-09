package pl.akademiaqa.bos.user.domain;

import lombok.Data;
import pl.akademiaqa.bos.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "roles")
public class Role extends BaseEntity {

    private String name;

    public Role(){}

    public Role(String name) {
        this.name = name;
    }
}
