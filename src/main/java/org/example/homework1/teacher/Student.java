package org.example.homework1.teacher;

public record Student(String name, double grade) {

    @Override
    public String toString() {
        return "Student" + name + " - Mark: " + grade;
    }
}
