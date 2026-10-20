package gradebook.models;

import gradebook.interfaces.Validatable;

import java.util.ArrayList;
import java.util.List;

public class StudyGroup implements Validatable {
    private long id;
    private String groupCode;
    private int course;
    private String specialty;
    private List<Student> students;

    public StudyGroup() {
        this.students = new ArrayList<>();
    }

    public StudyGroup(long id, String groupCode, int course, String specialty) {
        this.id = id;
        this.groupCode = groupCode;
        this.course = course;
        this.specialty = specialty;
        this.students = new ArrayList<>();
    }

    public StudyGroup(long id, String groupCode, int course, String specialty, List<Student> students) {
        this.id = id;
        this.groupCode = groupCode;
        this.course = course;
        this.specialty = specialty;
        this.students = students != null ? students : new ArrayList<>();
    }

    @Override
    public boolean validate() {
        // код групи типу ІПЗ-21, ПЗ-22, КН-31
        return groupCode != null && groupCode.matches("^[А-ЯІЇЄa-zA-Z]{2,4}-\\d{2,3}$");
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }

    public int getCourse() { return course; }
    public void setCourse(int course) { this.course = course; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}
