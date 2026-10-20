package gradebook.services;

import gradebook.models.assessment.Assessment;

public class ValidationService {

    public boolean isScoreCorrect(double score, double maxScore) {
        return maxScore > 0 && score >= 0 && score <= maxScore;
    }

    public boolean isTicketValid(String ticket) {
        if (ticket == null) {
            return false;
        }
        return ticket.matches("^[A-ZА-ЯІЇЄ]{2}\\d{8}$");
    }

    public boolean isGroupCodeValid(String code) {
        if (code == null) {
            return false;
        }
        return code.matches("^[А-ЯІЇЄa-zA-Z]{2,4}-\\d{2,3}$");
    }

    public boolean validateAssessment(Assessment a) {
        if (a == null) {
            return false;
        }
        if (a.getStudent() == null || a.getSubject() == null) {
            return false;
        }
        if (a.getTitle() == null || a.getTitle().trim().isEmpty()) {
            return false;
        }
        if (a.getDate() == null) {
            return false;
        }
        return isScoreCorrect(a.getScore(), a.getMaxScore());
    }
}
