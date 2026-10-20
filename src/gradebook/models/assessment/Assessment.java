package gradebook.models.assessment;

import gradebook.models.Student;
import gradebook.models.Subject;

import java.time.LocalDate;

public abstract class Assessment {
    private long id;
    private String title;
    private double score;
    private double maxScore;
    private LocalDate date;
    private Subject subject;
    private Student student;

    public Assessment() {
    }

    public Assessment(long id, String title, double score, double maxScore, LocalDate date, Subject subject, Student student) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.maxScore = maxScore;
        this.date = date;
        this.subject = subject;
        this.student = student;
    }

    public abstract boolean isPassed();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
