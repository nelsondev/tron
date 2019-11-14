package buildtools.exceptions;


import buildtools.utils.Utils;

public class SenderNoPermissionException extends Exception {
    public SenderNoPermissionException() {
        super(Utils.errorMessage("You have no permission for this command."));
    }
}

