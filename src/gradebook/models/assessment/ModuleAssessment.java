package gradebook.models.assessment;

import gradebook.models.Student;
import gradebook.models.Subject;

import java.time.LocalDate;

public class ModuleAssessment extends Assessment {
    private int moduleNumber;
    private double weight;

    public ModuleAssessment() {
        super();
    }

    public ModuleAssessment(long id, String title, double score, double maxScore, LocalDate date,
                            Subject subject, Student student, int moduleNumber, double weight) {
        super(id, title, score, maxScore, date, subject, student);
        this.moduleNumber = moduleNumber;
        this.weight = weight;
    }

    @Override
    public boolean isPassed() {
        if (getMaxScore() <= 0) {
            return false;
        }
        return (getScore() / getMaxScore() >= 0.6);
    }

    public int getModuleNumber() { return moduleNumber; }
    public void setModuleNumber(int moduleNumber) { this.moduleNumber = moduleNumber; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
}
