package gradebook.io;

import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.assessment.Assessment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReportExporter {

    public boolean exportStudentReport(Student s, String path) {
        if (s == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== Звіт по студенту ==========\n");
        sb.append("Студент: ").append(s.getLastName()).append(" ").append(s.getFirstName()).append("\n");
        sb.append("Квиток: ").append(s.getStudentTicketNumber()).append("\n");
        if (s.getGroup() != null) {
            sb.append("Група: ").append(s.getGroup().getGroupCode()).append("\n");
        }
        sb.append("Оцінки:\n");
        if (s.getAssessments().isEmpty()) {
            sb.append("  (оцінок ще немає)\n");
        }
        for (Assessment a : s.getAssessments()) {
            sb.append("  ").append(a.getTitle());
            if (a.getSubject() != null) {
                sb.append(" (").append(a.getSubject().getName()).append(")");
            }
            sb.append(" - ").append(a.getScore()).append("/").append(a.getMaxScore());
            sb.append(a.isPassed() ? " здано" : " НЕ ЗДАНО");
            if (a.getDate() != null) {
                sb.append(" від ").append(a.getDate());
            }
            sb.append("\n");
        }
        return writeToFile(sb.toString(), path);
    }

    public boolean exportGroupReport(StudyGroup g, List<Student> ranking, String path) {
        if (g == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== Звіт по групі ").append(g.getGroupCode()).append(" ==========\n");
        sb.append("Спеціальність: ").append(g.getSpecialty()).append("\n");
        sb.append("Курс: ").append(g.getCourse()).append("\n");
        sb.append("Студенти:\n");
        for (Student s : g.getStudents()) {
            sb.append("  ").append(s.getLastName()).append(" ").append(s.getFirstName());
            if (ranking != null) {
                int pos = ranking.indexOf(s);
                if (pos >= 0) {
                    sb.append(" (місце в рейтингу: ").append(pos + 1).append(")");
                }
            }
            sb.append("\n");
            for (Assessment a : s.getAssessments()) {
                sb.append("    - ").append(a.getTitle()).append(": ")
                  .append(a.getScore()).append("/").append(a.getMaxScore())
                  .append(a.isPassed() ? " здано" : " НЕ ЗДАНО").append("\n");
            }
        }
        return writeToFile(sb.toString(), path);
    }

    public boolean exportRanking(List<Student> ranking, String path) {
        if (ranking == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== Рейтинг студентів ==========\n");
        int place = 1;
        for (Student s : ranking) {
            sb.append(place).append(". ").append(s.getLastName()).append(" ").append(s.getFirstName());
            if (s.getGroup() != null) {
                sb.append(" (").append(s.getGroup().getGroupCode()).append(")");
            }
            sb.append("\n");
            place++;
        }
        return writeToFile(sb.toString(), path);
    }

    public boolean exportDebtors(List<Student> debtors, String path) {
        if (debtors == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== Список боржників ==========\n");
        if (debtors.isEmpty()) {
            sb.append("Боржників немає\n");
        }
        for (Student s : debtors) {
            sb.append("- ").append(s.getLastName()).append(" ").append(s.getFirstName());
            if (s.getGroup() != null) {
                sb.append(" (").append(s.getGroup().getGroupCode()).append(")");
            }
            sb.append("\n");
            for (Assessment a : s.getAssessments()) {
                if (!a.isPassed()) {
                    sb.append("    не здано: ").append(a.getTitle()).append("\n");
                }
            }
        }
        return writeToFile(sb.toString(), path);
    }

    private boolean writeToFile(String text, String path) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            bw.write(text);
            System.out.println("Звіт збережено у файл: " + path);
            return true;
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
            return false;
        }
    }
}
