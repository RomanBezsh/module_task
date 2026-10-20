package gradebook.io;

import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.Subject;
import gradebook.models.Teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataImporter {

    // формат: id,квиток,ім'я,прізвище,кодГрупи
    public List<Student> importStudents(String path, List<StudyGroup> groups) {
        List<Student> res = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < 5) {
                    System.out.println("Пропущено рядок (мало полів): " + line);
                    continue;
                }
                String ticket = p[1].trim();
                boolean dup = false;
                for (Student s : res) {
                    if (s.getStudentTicketNumber().equals(ticket)) {
                        dup = true;
                        break;
                    }
                }
                if (dup) {
                    System.out.println("Пропущено дублікат: " + ticket);
                    continue;
                }
                StudyGroup g = null;
                String gcode = p[4].trim();
                for (StudyGroup gg : groups) {
                    if (gg.getGroupCode().equalsIgnoreCase(gcode)) {
                        g = gg;
                        break;
                    }
                }
                if (g == null) {
                    System.out.println("Пропущено студента, немає групи: " + gcode);
                    continue;
                }
                Student s = new Student(0, ticket, p[2].trim(), p[3].trim(), g);
                if (!s.validate()) {
                    System.out.println("Пропущено некоректний запис: " + line);
                    continue;
                }
                res.add(s);
            }
        } catch (IOException e) {
            System.out.println("Не вдалося прочитати файл студентів: " + e.getMessage());
        }
        return res;
    }

    // формат: id,код,назва,кредити,ім'яВикладача,прізвищеВикладача,звання
    public List<Subject> importSubjects(String path) {
        List<Subject> res = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < 7) {
                    System.out.println("Пропущено рядок (мало полів): " + line);
                    continue;
                }
                String code = p[1].trim();
                boolean dup = false;
                for (Subject sub : res) {
                    if (sub.getCode().equalsIgnoreCase(code)) {
                        dup = true;
                        break;
                    }
                }
                if (dup) {
                    System.out.println("Пропущено дублікат предмета: " + code);
                    continue;
                }
                int credits;
                try {
                    credits = Integer.parseInt(p[3].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Пропущено рядок (кредити не число): " + line);
                    continue;
                }
                if (credits <= 0) {
                    System.out.println("Пропущено рядок (кредити <= 0): " + line);
                    continue;
                }
                Teacher t = new Teacher(0, p[4].trim(), p[5].trim(), p[6].trim());
                Subject sub = new Subject(0, code, p[2].trim(), credits, t);
                res.add(sub);
            }
        } catch (IOException e) {
            System.out.println("Не вдалося прочитати файл предметів: " + e.getMessage());
        }
        return res;
    }
}
