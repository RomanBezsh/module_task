package gradebook.models;

public class Subject {
    private long id;
    private String code;
    private String name;
    private int credits;
    private Teacher teacher;

    public Subject() {
    }

    public Subject(long id, String code, String name, int credits, Teacher teacher) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.teacher = teacher;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
}
