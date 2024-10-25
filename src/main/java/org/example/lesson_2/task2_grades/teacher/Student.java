package org.example.lesson_2.task2_grades.teacher;

public record Student(String name, double grade) {

    @Override
    public String toString() {
        return "Student" + name + " - Mark: " + grade;
    }
}
