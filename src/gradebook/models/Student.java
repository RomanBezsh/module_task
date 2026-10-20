package gradebook.models;

import gradebook.interfaces.Validatable;
import gradebook.models.assessment.Assessment;

import java.util.ArrayList;
import java.util.List;

public class Student implements Validatable {
    private long id;
    private String studentTicketNumber;
    private String firstName;
    private String lastName;
    private StudyGroup group;
    private List<Assessment> assessments;

    public Student() {
        this.assessments = new ArrayList<>();
    }

    public Student(long id, String studentTicketNumber, String firstName, String lastName, StudyGroup group) {
        this.id = id;
        this.studentTicketNumber = studentTicketNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.assessments = new ArrayList<>();
    }

    public Student(long id, String studentTicketNumber, String firstName, String lastName, StudyGroup group, List<Assessment> assessments) {
        this.id = id;
        this.studentTicketNumber = studentTicketNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.assessments = assessments != null ? assessments : new ArrayList<>();
    }

    @Override
    public boolean validate() {
        // квиток має виглядати як КВ12345678 - дві літери і8 цифр
        if (studentTicketNumber == null || !studentTicketNumber.matches("^[A-ZА-ЯІЇЄ]{2}\\d{8}$")) {
            return false;
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            return false;
        }
        return lastName != null && !lastName.trim().isEmpty();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getStudentTicketNumber() { return studentTicketNumber; }
    public void setStudentTicketNumber(String studentTicketNumber) { this.studentTicketNumber = studentTicketNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public StudyGroup getGroup() { return group; }
    public void setGroup(StudyGroup group) { this.group = group; }

    public List<Assessment> getAssessments() { return assessments; }
    public void setAssessments(List<Assessment> assessments) { this.assessments = assessments; }
}
