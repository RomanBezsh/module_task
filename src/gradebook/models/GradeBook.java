package gradebook.models;

import gradebook.interfaces.Exportable;
import gradebook.models.assessment.Assessment;

import java.util.ArrayList;
import java.util.List;

public class GradeBook implements Exportable {
    private long id;
    private Student student;
    private List<Assessment> records;

    public GradeBook() {
        this.records = new ArrayList<>();
    }

    public GradeBook(long id, Student student) {
        this.id = id;
        this.student = student;
        this.records = new ArrayList<>();
    }

    public GradeBook(long id, Student student, List<Assessment> records) {
        this.id = id;
        this.student = student;
        this.records = records != null ? records : new ArrayList<>();
    }

    public void addAssessment(Assessment assessment) {
        if (this.records == null) {
            this.records = new ArrayList<>();
        }
        this.records.add(assessment);
    }

    @Override
    public String exportData() {
        StringBuilder sb = new StringBuilder();
        sb.append("Залікова книжка ID: ").append(id).append("\n");
        if (student != null) {
            sb.append("Студент: ").append(student.getLastName()).append(" ").append(student.getFirstName());
            sb.append(" (").append(student.getStudentTicketNumber()).append(")\n");
        } else {
            sb.append("Студент: не вказано\n");
        }
        sb.append("Записи:\n");
        if (records.isEmpty()) {
            sb.append("  (немає)\n");
        }
        for (Assessment a : records) {
            sb.append("- ").append(a.getTitle()).append(": ")
              .append(a.getScore()).append("/").append(a.getMaxScore())
              .append(a.isPassed() ? " [здано]" : " [не здано]").append("\n");
        }
        return sb.toString();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public List<Assessment> getRecords() { return records; }
    public void setRecords(List<Assessment> records) { this.records = records; }
}
