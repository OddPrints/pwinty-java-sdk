package uk.co.mattburns.pwinty.model;

import java.util.List;

public class SubmissionStatus {
    private int id;
    private boolean isValid;
    private List<PhotoStatus> photos;
    private List<GeneralError> generalErrors;

    public enum GeneralError {
        AccountBalanceInsufficient, ItemsContainingErrors, NoItemsInOrder, PostalAddressNotSet;
    }

    public enum PhotoError {
        FileCouldNotBeDownloaded, NoImageFile, InvalidImagefile, PostalAddressNotSet;
    }

    public enum PhotoWarning {
        CroppingWillOccur, PictureSizeTooSmall, CouldNotValidateImageSize, CouldNotValidateAspectRatio;
    }

    public int getId() {
        return id;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<PhotoStatus> getPhotos() {
        return photos;
    }

    public List<GeneralError> getGeneralErrors() {
        return generalErrors;
    }

    @Override
    public String toString() {
        return "SubmissionStatus [id=" + id + ", isValid=" + isValid
                + ", photos=" + photos + ", generalErrors=" + generalErrors
                + "]";
    }

}
