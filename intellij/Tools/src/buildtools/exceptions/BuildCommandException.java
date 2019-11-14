package buildtools.exceptions;

import buildtools.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BuildCommandException extends Exception {
    private static final long serialVersionUID = -466449116370648018L;
    // exception return message
    static String exception = "";

    // display full help constructor
    public BuildCommandException() {
        super(message());
    }

    // display specific command help constructor
    public BuildCommandException(BuildHelp help) {
        super(message(help));
    }

    public enum BuildHelp {

        SETTING(0),
        CLEAR(1),
        BUILD(2),
        REPLACE(3),
        BLOCK(4);

        private final int result;

        BuildHelp(int result) {
            this.result = result;
        }

        public int val() {
            return result;
        }
    }

    private static class Help {

        static final String setting = Utils.errorMessage("You can set all blocks between two positions with /build set <block>. An example would be /build set iron ore");
        static final String clear = Utils.errorMessage("You already have a build. You can remove it with /build clear");
        static final String build = Utils.errorMessage("Builds can be created with the command /build pos. You must set a first and second position");
        static final String replace = Utils.errorMessage("You can replace all blocks between two positions with /build replace <block> with <block>. Please note the \"with\" operator. It is needed. An example would be /build replace stone with iron ore");
        static final String block = Utils.errorMessage("The block parameter within the /build set, and replace command require block names. A block name is defined as it would be named within MineCraft usually. An example would be \"gold block\"");
        static final List<String> help = new ArrayList<>(Utils.stringsToArray(setting, clear, build, replace, block));

        public static String getHelp() {
            return Utils.arrayToString(help, "\n");
        }

        public static String getHelp(BuildHelp buildHelp) {
            return help.get(buildHelp.val());
        }
    }

    // default help page for sell sub command.
    static String message() {
        exception = Help.getHelp();

        return exception;
    }

    static String message(BuildHelp help) {
        exception = Help.getHelp(help);

        return exception;
    }
}
