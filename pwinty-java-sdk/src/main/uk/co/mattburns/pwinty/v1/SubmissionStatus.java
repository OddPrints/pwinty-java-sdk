package uk.co.mattburns.pwinty.v1;

import java.util.List;

@Deprecated
public class SubmissionStatus {
    private int id;
    private boolean isValid;
    private List<PhotoStatus> photos;
    private List<GeneralError> generalErrors;

    @Deprecated
    public enum GeneralError {
        AccountBalanceInsufficient, ItemsContainingErrors, NoItemsInOrder, PostalAddressNotSet;
    }

    @Deprecated
    public enum PhotoError {
        FileCouldNotBeDownloaded, NoImageFile, InvalidImagefile, PostalAddressNotSet;
    }

    @Deprecated
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
