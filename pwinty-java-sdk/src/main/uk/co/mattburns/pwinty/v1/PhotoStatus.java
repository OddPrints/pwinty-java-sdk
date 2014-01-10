package uk.co.mattburns.pwinty.v1;

import java.util.List;

import uk.co.mattburns.pwinty.v1.SubmissionStatus.PhotoError;
import uk.co.mattburns.pwinty.v1.SubmissionStatus.PhotoWarning;

@Deprecated
public class PhotoStatus {
    private int id;
    private List<PhotoError> errors;
    private List<PhotoWarning> warnings;

    public int getId() {
        return id;
    }

    public List<PhotoError> getErrors() {
        return errors;
    }

    public List<PhotoWarning> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "PhotoStatus [id=" + id + ", errors=" + errors + ", warnings="
                + warnings + "]";
    }

}