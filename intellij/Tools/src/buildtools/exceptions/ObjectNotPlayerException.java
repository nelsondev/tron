package buildtools.exceptions;

import buildtools.utils.Utils;

public class ObjectNotPlayerException extends Exception {
    public ObjectNotPlayerException() {
        super(Utils.errorMessage("You have no permission for this command"));
    }
}
