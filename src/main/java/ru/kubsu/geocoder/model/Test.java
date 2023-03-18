package ru.kubsu.geocoder.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Bogdan Lesin
 */
@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;
    private Boolean done;

    @Enumerated(EnumType.STRING)
    private Mark mark;


    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(final Boolean done) {
        this.done = done;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(final Mark mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Test test = (Test) o;
        return id.equals(test.id) && Objects.equals(name, test.name)
                && Objects.equals(done, test.done) && mark == test.mark;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, done, mark);
    }

    @Override
    public String toString() {
        return "Test{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", done=" + done
                + ", mark=" + mark
                + '}';
    }
}
