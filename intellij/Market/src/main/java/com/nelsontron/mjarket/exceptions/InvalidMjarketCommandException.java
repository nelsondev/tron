package com.nelsontron.mjarket.exceptions;

import com.nelsontron.mjarket.utils.Utils;

public class InvalidMjarketCommandException extends Exception {

    private static final long serialVersionUID = 3404556705581499381L;

    public InvalidMjarketCommandException() {
        super(Utils.errorMessage("Something went wrong with your command usage. Try /market for help.", "/market"));
    }

    public InvalidMjarketCommandException(String string, String command) {
        super(Utils.errorMessage("Invalid command usage '" + string + "' in command '/market " + command + ".'", string));
    }
}