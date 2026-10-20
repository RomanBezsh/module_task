package gradebook.models;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private long id;
    private String firstName;
    private String lastName;
    private String academicTitle;
    private List<Subject> taughtSubjects;

    public Teacher() {
        this.taughtSubjects = new ArrayList<>();
    }

    public Teacher(long id, String firstName, String lastName, String academicTitle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.academicTitle = academicTitle;
        this.taughtSubjects = new ArrayList<>();
    }

    public Teacher(long id, String firstName, String lastName, String academicTitle, List<Subject> taughtSubjects) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.academicTitle = academicTitle;
        this.taughtSubjects = taughtSubjects != null ? taughtSubjects : new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAcademicTitle() { return academicTitle; }
    public void setAcademicTitle(String academicTitle) { this.academicTitle = academicTitle; }

    public List<Subject> getTaughtSubjects() { return taughtSubjects; }
    public void setTaughtSubjects(List<Subject> taughtSubjects) { this.taughtSubjects = taughtSubjects; }
}
