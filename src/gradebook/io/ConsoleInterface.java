package gradebook.io;

import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.Subject;
import gradebook.models.Teacher;
import gradebook.models.assessment.Assessment;
import gradebook.models.assessment.ExamAssessment;
import gradebook.models.assessment.LabAssessment;
import gradebook.models.assessment.ModuleAssessment;
import gradebook.models.assessment.TestAssessment;
import gradebook.services.GradeManagementService;
import gradebook.services.StatisticsService;
import gradebook.services.ValidationService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInterface {
    private final Scanner in;
    private final GradeManagementService gms;
    private final StatisticsService stats;
    private final ValidationService vs;
    private final ReportExporter exporter;

    public ConsoleInterface(GradeManagementService gms, StatisticsService stats, ValidationService vs) {
        this.in = new Scanner(System.in);
        this.gms = gms;
        this.stats = stats;
        this.vs = vs;
        this.exporter = new ReportExporter();
    }

    public void run() {
        Map<String, Runnable> menu = new LinkedHashMap<>();
        menu.put("1", this::listStudents);
        menu.put("2", this::searchById);
        menu.put("3", this::searchByLastName);
        menu.put("4", this::searchByGroup);
        menu.put("5", this::addGroup);
        menu.put("6", this::addStudent);
        menu.put("7", this::addAssessment);
        menu.put("8", this::showStudentAverage);
        menu.put("9", this::showGroupAverage);
        menu.put("10", this::showRating);
        menu.put("11", this::showDebtors);
        menu.put("12", this::showLowestSubject);
        menu.put("13", this::doImport);
        menu.put("14", this::doExport);

        boolean work = true;
        while (work) {
            printMenu();
            String choice = in.nextLine().trim();
            if ("0".equals(choice)) {
                work = false;
                continue;
            }
            Runnable act = menu.get(choice);
            if (act != null) {
                act.run();
            } else {
                System.out.println("Невідомий пункт меню, спробуйте ще раз");
            }
        }
        System.out.println("Дякуємо за роботу, до побачення!");
        in.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== Система обліку успішності =====");
        System.out.println("1 - показати всіх студентів");
        System.out.println("2 - пошук студента за id");
        System.out.println("3 - пошук за прізвищем");
        System.out.println("4 - пошук за групою");
        System.out.println("5 - додати групу");
        System.out.println("6 - додати студента");
        System.out.println("7 - додати оцінку");
        System.out.println("8 - середній бал студента");
        System.out.println("9 - середній бал групи");
        System.out.println("10 - рейтинг");
        System.out.println("11 - боржники");
        System.out.println("12 - предмет із найнижчими результатами");
        System.out.println("13 - імпорт з файлу");
        System.out.println("14 - експорт звіту у файл");
        System.out.println("0 - вихід");
        System.out.print("Ваш вибір: ");
    }

    private void listStudents() {
        List<Student> all = gms.getStudents();
        if (all.isEmpty()) {
            System.out.println("Список студентів порожній");
            return;
        }
        for (Student s : all) {
            printStudent(s);
        }
    }

    private void printStudent(Student s) {
        System.out.print("[" + s.getId() + "] " + s.getLastName() + " " + s.getFirstName()
                + ", квиток " + s.getStudentTicketNumber());
        if (s.getGroup() != null) {
            System.out.print(", група " + s.getGroup().getGroupCode());
        }
        System.out.println();
    }

    private void searchById() {
        long id = readLong("Введіть id студента: ");
        Student s = gms.findStudentById(id);
        if (s == null) {
            System.out.println("Студента з таким id не знайдено");
        } else {
            printStudent(s);
            printAssessments(s);
        }
    }

    private void searchByLastName() {
        System.out.print("Введіть прізвище: ");
        String last = in.nextLine().trim();
        List<Student> found = gms.findStudentsByLastName(last);
        if (found.isEmpty()) {
            System.out.println("Нікого не знайдено");
        } else {
            for (Student s : found) {
                printStudent(s);
            }
        }
    }

    private void searchByGroup() {
        System.out.print("Введіть код групи: ");
        String code = in.nextLine().trim();
        List<Student> found = gms.findStudentsByGroup(code);
        if (found.isEmpty()) {
            System.out.println("У цій групі студентів немає (або група не існує)");
        } else {
            for (Student s : found) {
                printStudent(s);
            }
        }
    }

    private void printAssessments(Student s) {
        if (s.getAssessments().isEmpty()) {
            System.out.println("  Оцінок ще немає");
            return;
        }
        for (Assessment a : s.getAssessments()) {
            System.out.println("  - " + a.getTitle() + ": " + a.getScore() + "/" + a.getMaxScore()
                    + (a.isPassed() ? " [здано]" : " [НЕ ЗДАНО]"));
        }
    }

    private void addGroup() {
        System.out.print("Код групи (напр. ІПЗ-21): ");
        String code = in.nextLine().trim();
        int course = readInt("Курс: ");
        System.out.print("Спеціальність: ");
        String spec = in.nextLine().trim();
        StudyGroup g = new StudyGroup(0, code, course, spec);
        if (!vs.isGroupCodeValid(code)) {
            System.out.println("Некоректний код групи, приклад: ІПЗ-21");
            return;
        }
        if (gms.registerGroup(g)) {
            System.out.println("Групу додано, id = " + g.getId());
        } else {
            System.out.println("Така група вже існує");
        }
    }

    private void addStudent() {
        if (gms.getGroups().isEmpty()) {
            System.out.println("Спочатку додайте групу");
            return;
        }
        for (StudyGroup g : gms.getGroups()) {
            System.out.println("  [" + g.getId() + "] " + g.getGroupCode());
        }
        System.out.print("id групи: ");
        long gid = readLong("");
        StudyGroup g = null;
        for (StudyGroup x : gms.getGroups()) {
            if (x.getId() == gid) {
                g = x;
                break;
            }
        }
        if (g == null) {
            System.out.println("Групу не знайдено");
            return;
        }
        System.out.print("Квиток (напр. КВ12345678): ");
        String ticket = in.nextLine().trim();
        System.out.print("Ім'я: ");
        String first = in.nextLine().trim();
        System.out.print("Прізвище: ");
        String last = in.nextLine().trim();
        Student s = new Student(0, ticket, first, last, g);
        if (gms.registerStudent(s)) {
            System.out.println("Студента додано, id = " + s.getId());
        } else {
            System.out.println("Не вдалося додати: перевірте формат квитка та щоб він не дублювався");
        }
    }

    private void addAssessment() {
        if (gms.getStudents().isEmpty() || gms.getSubjects().isEmpty()) {
            System.out.println("Потрібні хоча б один студент і один предмет");
            return;
        }
        System.out.print("id студента: ");
        long sid = readLong("");
        Student s = gms.findStudentById(sid);
        if (s == null) {
            System.out.println("Студента не знайдено");
            return;
        }
        System.out.println("Доступні предмети:");
        for (Subject sub : gms.getSubjects()) {
            System.out.println("  [" + sub.getId() + "] " + sub.getCode() + " - " + sub.getName());
        }
        System.out.print("id предмета: ");
        long subId = readLong("");
        Subject sub = null;
        for (Subject x : gms.getSubjects()) {
            if (x.getId() == subId) {
                sub = x;
                break;
            }
        }
        if (sub == null) {
            System.out.println("Предмет не знайдено");
            return;
        }
        System.out.println("Тип оцінки: 1 - лаб, 2 - тест, 3 - модуль, 4 - іспит");
        String type = in.nextLine().trim();
        System.out.print("Назва: ");
        String title = in.nextLine().trim();
        double score = readDouble("Бал: ");
        double max = readDouble("Максимальний бал: ");
        if (!vs.isScoreCorrect(score, max)) {
            System.out.println("Бал має бути від 0 до " + max);
            return;
        }
        System.out.print("Дата (2025-09-01, порожнє = сьогодні): ");
        String d = in.nextLine().trim();
        LocalDate date;
        try {
            date = d.isEmpty() ? LocalDate.now() : LocalDate.parse(d);
        } catch (Exception e) {
            date = LocalDate.now();
        }
        Assessment a = null;
        if ("1".equals(type)) {
            int num = readInt("Номер лаби: ");
            System.out.print("Захищено? (так/ні): ");
            boolean def = "так".equalsIgnoreCase(in.nextLine().trim());
            a = new LabAssessment(0, title, score, max, date, sub, s, num, def);
        } else if ("2".equals(type)) {
            int qc = readInt("Кількість питань: ");
            int ca = readInt("Правильних відповідей: ");
            a = new TestAssessment(0, title, score, max, date, sub, s, qc, ca);
        } else if ("3".equals(type)) {
            int num = readInt("Номер модуля: ");
            double w = readDouble("Вага: ");
            a = new ModuleAssessment(0, title, score, max, date, sub, s, num, w);
        } else if ("4".equals(type)) {
            Teacher ex = sub.getTeacher();
            System.out.print("Іспит фінальний? (так/ні): ");
            boolean fin = "так".equalsIgnoreCase(in.nextLine().trim());
            a = new ExamAssessment(0, title, score, max, date, sub, s, ex, fin);
        } else {
            System.out.println("Невідомий тип");
            return;
        }
        if (gms.addAssessment(a)) {
            System.out.println("Оцінку додано, id = " + a.getId()
                    + (a.isPassed() ? " (здано)" : " (не здано, студент - боржник)"));
        } else {
            System.out.println("Не вдалося додати оцінку");
        }
    }

    private void showStudentAverage() {
        System.out.print("id студента: ");
        long id = readLong("");
        Student s = gms.findStudentById(id);
        if (s == null) {
            System.out.println("Студента не знайдено");
            return;
        }
        double avg = stats.getStudentAverage(s);
        System.out.printf("Середній бал %s %s: %.2f%%%n", s.getLastName(), s.getFirstName(), avg);
    }

    private void showGroupAverage() {
        System.out.print("Код групи: ");
        String code = in.nextLine().trim();
        StudyGroup g = gms.findGroupByCode(code);
        if (g == null) {
            System.out.println("Групу не знайдено");
            return;
        }
        double avg = stats.getGroupAverage(g);
        System.out.printf("Середній бал групи %s: %.2f%%%n", g.getGroupCode(), avg);
    }

    private void showRating() {
        List<Student> ranking = stats.getRanking(gms.getStudents());
        if (ranking.isEmpty()) {
            System.out.println("Немає даних для рейтингу");
            return;
        }
        System.out.println("----- Рейтинг -----");
        int place = 1;
        for (Student s : ranking) {
            System.out.printf("%d. %s %s - %.2f%%%n", place, s.getLastName(), s.getFirstName(),
                    stats.getStudentAverage(s));
            place++;
        }
    }

    private void showDebtors() {
        List<Student> debtors = gms.getDebtors();
        if (debtors.isEmpty()) {
            System.out.println("Боржників немає, усе здано!");
            return;
        }
        System.out.println("----- Боржники -----");
        for (Student s : debtors) {
            printStudent(s);
            for (Assessment a : s.getAssessments()) {
                if (!a.isPassed()) {
                    System.out.println("    не здано: " + a.getTitle());
                }
            }
        }
        System.out.println("(щоб погасити борг, додайте нову оцінку через пункт 7)");
    }

    private void showLowestSubject() {
        Subject worst = stats.getLowestSubject(gms.getSubjects(), gms.getStudents());
        if (worst == null) {
            System.out.println("Немає даних по оцінках");
            return;
        }
        System.out.println("Найнижчі результати з предмета: " + worst.getName() + " (" + worst.getCode() + ")");
    }

    private void doImport() {
        System.out.println("1 - імпорт студентів, 2 - імпорт предметів");
        String ch = in.nextLine().trim();
        System.out.print("Шлях до файлу: ");
        String path = in.nextLine().trim();
        if ("1".equals(ch)) {
            int n = gms.importStudentsFromFile(path);
            System.out.println("Імпортовано студентів: " + n);
        } else if ("2".equals(ch)) {
            int n = gms.importSubjectsFromFile(path);
            System.out.println("Імпортовано предметів: " + n);
        } else {
            System.out.println("Невідомий вибір");
        }
    }

    private void doExport() {
        if (gms.getStudents().isEmpty()) {
            System.out.println("Немає даних для експорту");
            return;
        }
        System.out.println("1 - звіт по студенту, 2 - звіт по групі, 3 - рейтинг, 4 - боржники");
        String ch = in.nextLine().trim();
        System.out.print("Шлях до файлу: ");
        String path = in.nextLine().trim();
        if ("1".equals(ch)) {
            System.out.print("id студента: ");
            long id = readLong("");
            Student s = gms.findStudentById(id);
            if (s == null) {
                System.out.println("Студента не знайдено");
            } else {
                exporter.exportStudentReport(s, path);
            }
        } else if ("2".equals(ch)) {
            System.out.print("Код групи: ");
            String code = in.nextLine().trim();
            StudyGroup g = gms.findGroupByCode(code);
            if (g == null) {
                System.out.println("Групу не знайдено");
            } else {
                exporter.exportGroupReport(g, stats.getRanking(gms.getStudents()), path);
            }
        } else if ("3".equals(ch)) {
            exporter.exportRanking(stats.getRanking(gms.getStudents()), path);
        } else if ("4".equals(ch)) {
            exporter.exportDebtors(gms.getDebtors(), path);
        } else {
            System.out.println("Невідомий вибір");
        }
    }

    private int readInt(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        String s = in.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Це не число, вставлено 0");
            return 0;
        }
    }

    private long readLong(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        String s = in.nextLine().trim();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            System.out.println("Це не число, вставлено 0");
            return 0;
        }
    }

    private double readDouble(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        String s = in.nextLine().trim();
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            System.out.println("Це не число, вставлено 0");
            return 0;
        }
    }
}
