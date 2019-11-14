package com.nelsontron.mjarket.exceptions;

import com.nelsontron.mjarket.utils.Utils;

public class NoPermissionException extends Exception {

    public NoPermissionException() {
        super(Utils.errorMessage("You have no permission for this command."));
    }
}
