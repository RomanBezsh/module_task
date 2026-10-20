package gradebook.models.assessment;

import gradebook.models.Student;
import gradebook.models.Subject;

import java.time.LocalDate;

public class LabAssessment extends Assessment {
    private int labNumber;
    private boolean hasDefended;

    public LabAssessment() {
        super();
    }

    public LabAssessment(long id, String title, double score, double maxScore, LocalDate date,
                         Subject subject, Student student, int labNumber, boolean hasDefended) {
        super(id, title, score, maxScore, date, subject, student);
        this.labNumber = labNumber;
        this.hasDefended = hasDefended;
    }

    @Override
    public boolean isPassed() {
        if (getMaxScore() <= 0) {
            return false;
        }
        return hasDefended && (getScore() / getMaxScore() >= 0.6);
    }

    public int getLabNumber() { return labNumber; }
    public void setLabNumber(int labNumber) { this.labNumber = labNumber; }

    public boolean isHasDefended() { return hasDefended; }
    public void setHasDefended(boolean hasDefended) { this.hasDefended = hasDefended; }
}
