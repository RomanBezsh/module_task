package gradebook.services;

import gradebook.models.Student;
import gradebook.models.StudyGroup;
import gradebook.models.Subject;
import gradebook.models.assessment.Assessment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatisticsService {

    public double getStudentAverage(Student s) {
        if (s == null || s.getAssessments().isEmpty()) {
            return 0;
        }
        double total = 0;
        double maxTotal = 0;
        for (Assessment a : s.getAssessments()) {
            total += a.getScore();
            maxTotal += a.getMaxScore();
        }
        if (maxTotal == 0) {
            return 0;
        }
        return total / maxTotal * 100;
    }

    public double getGroupAverage(StudyGroup g) {
        if (g == null || g.getStudents().isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Student s : g.getStudents()) {
            sum += getStudentAverage(s);
        }
        return sum / g.getStudents().size();
    }

    public List<Student> getRanking(List<Student> all) {
        List<Student> sorted = new ArrayList<>(all);
        // рейтинг за спаданням середнього бала
        sorted.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return Double.compare(getStudentAverage(b), getStudentAverage(a));
            }
        });
        return sorted;
    }

    public List<Student> getDebtors(List<Student> all) {
        List<Student> res = new ArrayList<>();
        for (Student s : all) {
            for (Assessment a : s.getAssessments()) {
                if (!a.isPassed()) {
                    res.add(s);
                    break;
                }
            }
        }
        return res;
    }

    public Subject getLowestSubject(List<Subject> subjects, List<Student> all) {
        Subject worst = null;
        double worstAvg = Double.MAX_VALUE;
        for (Subject sub : subjects) {
            double total = 0;
            int count = 0;
            for (Student s : all) {
                for (Assessment a : s.getAssessments()) {
                    if (a.getSubject() != null && a.getSubject().getId() == sub.getId() && a.getMaxScore() > 0) {
                        total += a.getScore() / a.getMaxScore();
                        count++;
                    }
                }
            }
            if (count > 0) {
                double avg = total / count;
                if (avg < worstAvg) {
                    worstAvg = avg;
                    worst = sub;
                }
            }
        }
        return worst;
    }
}
