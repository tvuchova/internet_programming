package lesson_2.zadacha2_grades.teacher;

public record Student(String name, double grade) {

    @Override
    public String toString() {
        return "Student" + name + " - Mark: " + grade;
    }
}
