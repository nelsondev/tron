package com.nelsontron.mjarket.exceptions;

import com.nelsontron.mjarket.utils.Utils;

public class SenderNotPlayerException extends Exception {

    private static final long serialVersionUID = -4761636620559472868L;

    public SenderNotPlayerException() {
        super(Utils.errorMessage("Sender of this command must be a player."));
    }
}
