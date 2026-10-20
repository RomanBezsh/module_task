package gradebook.models.assessment;

import gradebook.models.Student;
import gradebook.models.Subject;

import java.time.LocalDate;

public class TestAssessment extends Assessment {
    private int questionCount;
    private int correctAnswers;

    public TestAssessment() {
        super();
    }

    public TestAssessment(long id, String title, double score, double maxScore, LocalDate date,
                          Subject subject, Student student, int questionCount, int correctAnswers) {
        super(id, title, score, maxScore, date, subject, student);
        this.questionCount = questionCount;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public boolean isPassed() {
        if (getMaxScore() <= 0) {
            return false;
        }
        return (getScore() / getMaxScore() >= 0.6);
    }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
}
