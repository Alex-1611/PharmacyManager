package Models;

import java.time.LocalDate;

public class Person {
    protected int id;
    protected String name;
    protected LocalDate dateOfBirth;

    public Person(int id, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}