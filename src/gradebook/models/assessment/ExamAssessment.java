package gradebook.models.assessment;

import gradebook.models.Student;
import gradebook.models.Subject;
import gradebook.models.Teacher;

import java.time.LocalDate;

public class ExamAssessment extends Assessment {
    private Teacher examiner;
    private boolean isFinal;

    public ExamAssessment() {
        super();
    }

    public ExamAssessment(long id, String title, double score, double maxScore, LocalDate date,
                          Subject subject, Student student, Teacher examiner, boolean isFinal) {
        super(id, title, score, maxScore, date, subject, student);
        this.examiner = examiner;
        this.isFinal = isFinal;
    }

    @Override
    public boolean isPassed() {
        if (getMaxScore() <= 0) {
            return false;
        }
        return (getScore() / getMaxScore() >= 0.6);
    }

    public Teacher getExaminer() { return examiner; }
    public void setExaminer(Teacher examiner) { this.examiner = examiner; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }
}
