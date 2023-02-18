package ru.kubsu.geocoder.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name="name", length = 50, nullable = false, unique = true)
    private String name;
    private Boolean done;

    @Enumerated(EnumType.STRING)
    private Mark mark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return id.equals(test.id) && Objects.equals(name, test.name) && Objects.equals(done, test.done) && mark == test.mark;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, done, mark);
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", done=" + done +
                ", mark=" + mark +
                '}';
    }
}
