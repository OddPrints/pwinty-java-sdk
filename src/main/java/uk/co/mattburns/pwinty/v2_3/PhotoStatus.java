package uk.co.mattburns.pwinty.v2_3;

import java.util.List;

public class PhotoStatus {
    private int id;
    private List<SubmissionStatus.PhotoError> errors;
    private List<SubmissionStatus.PhotoWarning> warnings;

    public int getId() {
        return id;
    }

    public List<SubmissionStatus.PhotoError> getErrors() {
        return errors;
    }

    public List<SubmissionStatus.PhotoWarning> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "PhotoStatus [id=" + id + ", errors=" + errors + ", warnings=" + warnings + "]";
    }
}
