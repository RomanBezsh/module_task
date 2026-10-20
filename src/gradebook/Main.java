package gradebook;

import gradebook.io.ConsoleInterface;
import gradebook.io.DataImporter;
import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.Subject;
import gradebook.models.Teacher;
import gradebook.models.assessment.ExamAssessment;
import gradebook.models.assessment.LabAssessment;
import gradebook.models.assessment.ModuleAssessment;
import gradebook.models.assessment.TestAssessment;
import gradebook.services.GradeManagementService;
import gradebook.services.StatisticsService;
import gradebook.services.ValidationService;

import java.time.LocalDate;

public class   Main {
    public static void main(String[] args) {
        ValidationService vs = new ValidationService();
        DataImporter importer = new DataImporter();
        GradeManagementService gms = new GradeManagementService(vs, importer);
        StatisticsService stats = new StatisticsService();

        fillDemoData(gms);

        System.out.println("Ласкаво просимо до системи обліку успішності!");
        System.out.println("Завантажено демодані: " + gms.getStudents().size() + " студентів, "
                + gms.getSubjects().size() + " предметів, " + gms.getGroups().size() + " груп");

        ConsoleInterface ui = new ConsoleInterface(gms, stats, vs);
        ui.run();
    }

    private static void fillDemoData(GradeManagementService gms) {
        StudyGroup g1 = new StudyGroup(0, "ІПЗ-21", 2, "Інженерія програмного забезпечення");
        StudyGroup g2 = new StudyGroup(0, "ПЗ-22", 1, "Комп'ютерні науки");
        gms.registerGroup(g1);
        gms.registerGroup(g2);

        Teacher t1 = new Teacher(0, "Наталія", "Гринчук", "доцент");
        Teacher t2 = new Teacher(0, "Андрій", "Лисенко", "професор");
        gms.registerTeacher(t1);
        gms.registerTeacher(t2);

        Subject sub1 = new Subject(0, "OOP1", "Об'єктно-орієнтоване програмування", 6, t1);
        Subject sub2 = new Subject(0, "DB1", "Бази даних", 4, t1);
        Subject sub3 = new Subject(0, "MA1", "Математичний аналіз", 6, t2);
        gms.registerSubject(sub1);
        gms.registerSubject(sub2);
        gms.registerSubject(sub3);

        Student s1 = new Student(0, "КВ12345678", "Іван", "Петренко", g1);
        Student s2 = new Student(0, "КВ23456789", "Марія", "Іваненко", g1);
        Student s3 = new Student(0, "КВ34567890", "Олег", "Сидоренко", g2);
        gms.registerStudent(s1);
        gms.registerStudent(s2);
        gms.registerStudent(s3);

        gms.addAssessment(new LabAssessment(0, "Лаб 1", 18, 20,
                LocalDate.of(2025, 9, 15), sub1, s1, 1, true));
        gms.addAssessment(new TestAssessment(0, "Тест 1", 16, 20,
                LocalDate.of(2025, 9, 22), sub1, s1, 20, 16));
        gms.addAssessment(new ModuleAssessment(0, "Модуль 1", 42, 50,
                LocalDate.of(2025, 10, 1), sub2, s1, 1, 0.5));
        gms.addAssessment(new ExamAssessment(0, "Іспит", 54, 60,
                LocalDate.of(2025, 12, 20), sub3, s1, t2, true));

        gms.addAssessment(new LabAssessment(0, "Лаб 1", 14, 20,
                LocalDate.of(2025, 9, 15), sub1, s2, 1, true));
        gms.addAssessment(new LabAssessment(0, "Лаб 2", 7, 20,
                LocalDate.of(2025, 10, 2), sub1, s2, 2, false));
        gms.addAssessment(new ModuleAssessment(0, "Модуль 1", 28, 50,
                LocalDate.of(2025, 10, 1), sub2, s2, 1, 0.5));

        gms.addAssessment(new TestAssessment(0, "Тест 1", 10, 20,
                LocalDate.of(2025, 9, 22), sub1, s3, 20, 10));
        gms.addAssessment(new ExamAssessment(0, "Іспит", 48, 60,
                LocalDate.of(2025, 12, 20), sub3, s3, t2, false));
    }
}
