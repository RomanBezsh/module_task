package gradebook.services;

import gradebook.io.DataImporter;
import gradebook.models.GradeBook;
import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.Subject;
import gradebook.models.Teacher;
import gradebook.models.assessment.Assessment;

import java.util.ArrayList;
import java.util.List;

public class GradeManagementService {
    private final List<Student> students;
    private final List<StudyGroup> groups;
    private final List<Subject> subjects;
    private final List<Teacher> teachers;
    private final List<GradeBook> gradeBooks;
    private final ValidationService validationService;
    private final DataImporter dataImporter;
    private long idCounter;

    public GradeManagementService(ValidationService validationService, DataImporter dataImporter) {
        this.students = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.gradeBooks = new ArrayList<>();
        this.validationService = validationService;
        this.dataImporter = dataImporter;
        this.idCounter = 1;
    }

    private long nextId() {
        return idCounter++;
    }

    public List<Student> getStudents() { return students; }
    public List<StudyGroup> getGroups() { return groups; }
    public List<Subject> getSubjects() { return subjects; }
    public List<Teacher> getTeachers() { return teachers; }
    public List<GradeBook> getGradeBooks() { return gradeBooks; }

    public boolean registerGroup(StudyGroup g) {
        if (g == null || !validationService.isGroupCodeValid(g.getGroupCode())) {
            return false;
        }
        for (StudyGroup x : groups) {
            if (x.getGroupCode().equalsIgnoreCase(g.getGroupCode())) {
                return false;
            }
        }
        g.setId(nextId());
        groups.add(g);
        return true;
    }

    public boolean registerTeacher(Teacher t) {
        if (t == null) {
            return false;
        }
        t.setId(nextId());
        teachers.add(t);
        return true;
    }

    public boolean registerStudent(Student s) {
        if (s == null || !s.validate() || s.getGroup() == null) {
            return false;
        }
        for (Student x : students) {
            if (x.getStudentTicketNumber().equals(s.getStudentTicketNumber())) {
                return false;
            }
        }
        s.setId(nextId());
        students.add(s);
        s.getGroup().getStudents().add(s);
        gradeBooks.add(new GradeBook(nextId(), s));
        return true;
    }

    public boolean registerSubject(Subject sub) {
        if (sub == null || sub.getCode() == null || sub.getName() == null) {
            return false;
        }
        if (sub.getCredits() <= 0) {
            return false;
        }
        for (Subject x : subjects) {
            if (x.getCode().equalsIgnoreCase(sub.getCode())) {
                return false;
            }
        }
        sub.setId(nextId());
        subjects.add(sub);
        if (sub.getTeacher() != null && !teachers.contains(sub.getTeacher())) {
            teachers.add(sub.getTeacher());
        }
        return true;
    }

    public boolean addAssessment(Assessment a) {
        if (!validationService.validateAssessment(a)) {
            return false;
        }
        a.setId(nextId());
        a.getStudent().getAssessments().add(a);
        for (GradeBook gb : gradeBooks) {
            if (gb.getStudent().getId() == a.getStudent().getId()) {
                gb.addAssessment(a);
            }
        }
        return true;
    }

    public Student findStudentById(long id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public List<Student> findStudentsByLastName(String lastName) {
        List<Student> found = new ArrayList<>();
        if (lastName == null) {
            return found;
        }
        for (Student s : students) {
            if (s.getLastName().equalsIgnoreCase(lastName.trim())) {
                found.add(s);
            }
        }
        return found;
    }

    public List<Student> findStudentsByGroup(String groupCode) {
        List<Student> found = new ArrayList<>();
        if (groupCode == null) {
            return found;
        }
        for (Student s : students) {
            if (s.getGroup() != null && s.getGroup().getGroupCode().equalsIgnoreCase(groupCode.trim())) {
                found.add(s);
            }
        }
        return found;
    }

    public StudyGroup findGroupByCode(String code) {
        if (code == null) {
            return null;
        }
        for (StudyGroup g : groups) {
            if (g.getGroupCode().equalsIgnoreCase(code.trim())) {
                return g;
            }
        }
        return null;
    }

    public List<Student> getDebtors() {
        List<Student> res = new ArrayList<>();
        for (Student s : students) {
            for (Assessment a : s.getAssessments()) {
                if (!a.isPassed()) {
                    res.add(s);
                    break;
                }
            }
        }
        return res;
    }

    public boolean isDebtor(Student s) {
        if (s == null) {
            return false;
        }
        for (Assessment a : s.getAssessments()) {
            if (!a.isPassed()) {
                return true;
            }
        }
        return false;
    }

    public int importStudentsFromFile(String path) {
        List<Student> imported = dataImporter.importStudents(path, groups);
        int ok = 0;
        for (Student s : imported) {
            if (registerStudent(s)) {
                ok++;
            }
        }
        return ok;
    }

    public int importSubjectsFromFile(String path) {
        List<Subject> imported = dataImporter.importSubjects(path);
        int ok = 0;
        for (Subject sub : imported) {
            if (registerSubject(sub)) {
                ok++;
            }
        }
        return ok;
    }
}
