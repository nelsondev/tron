var mansole = function() {

    //
    // Mansole requires nel.tils.js to work.
    //
    var nt = require('./nel.tils.js');

    //
    //  Settings object used to store useful variables.
    //
    var settings = {
        inputElement: "#mansole-input",
        outputElement: "#mansole-output",
        lineHeight: 24,
        loginMessage: [
            ';bManny Console',
            'v0.0.1 - dla - nelson.m;n',
        ]
    }

    // Define mansole input and output objects
    var $input = nt.element(settings.inputElement),
        $output = nt.element(settings.outputElement);

    //
    // common mansole utility functions
    //
    function format(str, preformatted) {
        let conf = require("./mans.html.conf.js");

        let arr = Object.keys(conf);
        for (let i in arr) {
            var replaceWith = Object.keys(conf[arr[i]]);
            var replace = Object.values(conf[arr[i]]);
            str = str.replace(replace[0], replaceWith[0]);
            str = str.replace(replace[1], replaceWith[1]);
        }

        if (preformatted) {
            str = "<pre>" + str + "</pre>";
        } else {
            str = "<div class='mansole-text'>" + str + "</div>";
        }

        return str;
    }

    function append(obj, str) {
        obj.innerHTML = obj.innerHTML + str;
    }

    var terminal = function() {
        function e(a, c) {
            if (Array.isArray(a))
                for (var d in a) {
                    var b = format(a[d], c);
                    append($output, b)
                } else b = format(a, c), append($output, b);
            window.scrollTo(0, $output.scrollHeight)
        }
        return {
            log: e,
            clear: function() {
                $output.innerHTML = ""
            },
            space: function() {
                for (var a = 0; a < settings.lineHeight; a++) e(";n")
            },
            commandListener: function(a, c) {
                var d = a.keyCode ? a.keyCode : a.which,
                    b = nt.sanitizeHtml($input.value);
                "13" == d && (c(b), $input.value = "")
            }
        }
    }();

    //
    // Terminal function returning all terminal related commands.
    //
    var terminal = function() {

        function log(str, preformatted) {
            var output = "";
            if (Array.isArray(str)) {
                for (var i in str) {
                    output = format(str[i], preformatted);
                    append($output, output);
                }
            } else {
                output = format(str, preformatted);
                append($output, output);
            }
            scrollTop();
        }

        function clear() {
            $output.innerHTML = "";
        }

        function space() {
            for (var i = 0; i < settings.lineHeight; i++)
                log(";n");
        }

        function scrollTop() {
            window.scrollTo(0, $output.scrollHeight);
        }

        // Register command handler for text input.
        function registerCommandHandler(event, callback) {
            var key = (event.keyCode ? event.keyCode : event.which);
            var val = nt.sanitizeHtml($input.value);

            if (key == '13') {
                callback(val);
                $input.value = "";
            }
        }

        return {
            log: log,
            clear: clear,
            space: space,
            commandListener: registerCommandHandler
        }
    }();

    //
    // Input object related functions
    //
    var input = function() {

        function focusObject() {
            $input.focus()
        }

        function clearObject() {
            $input.innerHTML = "";
        }

        return {
            focus: focusObject,
            clear: clearObject,
        }

    }();

    // Register event listeners needed for mansole to run.
    function run(func) {
        terminal.log(settings.loginMessage);
        nt.listen(window, "load keydown", input.focus);
        nt.listen($input, "keydown", () => terminal.commandListener(event, func));
    }

    return {
        run: run,
        term: terminal,
    }
}();

module.exports = {
    run: mansole.run,
    term: mansole.term,
    log: mansole.term.log,
}