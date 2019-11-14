// mannys room object
var room = function() {

    var nt = require('./nel.tils.js'),
        mans = require("./mansole.js"),
        mansole = mans,
        manny = require("./js/manny.js"),
        memory = manny.memory,
        mem = memory,
        player = manny.player,
        pl = player

    var recordCounter = 0;

    function record() {
        var text = [
            ';nOH god, that was loud.',
            ';nLet me turn this down first.',
            ";nThat's about enough of that."
        ];
        first = function() {
            mansole.term.log(text[0]);
            recordCounter++;
        };
        second = function() {
            mansole.term.log(text[1]);
            recordCounter++;
        };
        third = function() {
            mansole.term.log(text[2]);
            recordCounter = 1;
        };
        if (recordCounter == 0)
            first();
        else
        if (recordCounter == 1)
            second();
        else
        if (recordCounter == 2)
            third();
    }

    function lockbox() {
        let text = [
            ";nThe lockbox is cold, covered in dust and looks unopened. There's a key hole on the front.",
            ';nThe key fits perfectly. The lockbox seems to be relatively empty and light. Just containing a photo and some trinkets from your childhood you’ve had since you remember. An empty bottle of pills stands behind where the lockbox had been placed. Reading “Aripiprazole, take once daily.” Your memory feels a bit foggy. I should go to the hallway.',
            ';g;n    + Empty pill bottle has been added to inventory.g;'
        ];
        first = function() {
            mansole.term.log(text[0]);
        };
        second = function() {
            mansole.term.log(text[1]);
            mansole.term.log(text[2], true);
            addItem('empty pill bottle');
            mem.substate = ""; //reset sub location
        };
        if ('lockbox key' in player.inv)
            second();
        else
            first();
    }

    function dresser() {
        let text = [
            ";nI wonder what I should wear today…",
            ";nI need some pants now.",
            ";nYou find a key in your pocket. That’s where it's been. Looking at the clock displayed on your computer it reads 3:42AM, who would be up this late? Certainly not your roommates.",
            ";g;n    + Key has been added to your inventory.g;"
        ];
        let shirts = [
            ";nBlouse",
            "Denim Vest",
            "Hawaiian",
            "Tuxedo T-Shirt",
        ];
        let pants = [
            ";nZipoff Pants",
            "Jorts",
            "Lulu Leggings",
            "Swim Trunks"
        ];
        selectShirt = function() {
            mansole.term.log(text[0]);
            mansole.term.log(shirts);
            //listen for shirt input
            mem.substate = "dresser shirts";
        };
        selectPants = function() {
            mansole.term.log(text[1]);
            mansole.term.log(pants);
            //listen for pants input.
            mem.substate = "dresser pants";
        };
        finish = function() {
            mansole.term.log(text[2]);
            mansole.term.log(text[3], true);
            addItem("lockbox key");
            mem.substate = ""; //reset sub location
            //move on to hallway.
        };
        if (!player.inv.shirt)
            selectShirt();
        else
        if (!player.inv.pants)
            selectPants();
        else
            finish();
    }

    // commands for mannys room
    function main(val) {
        if (mem.state == "intro") {

            if (mem.substate == "dresser shirts") {

                if (val.charAt(0) == "b") {
                    pl.inv.shirt = "blouse";

                } else if (val.charAt(0) == "d") {
                    pl.inv.shirt = "denim vest";

                } else if (val.charAt(0) == "h") {
                    pl.inv.shirt = "hawaiian";

                } else if (val.charAt(0) == "t") {
                    pl.inv.shirt = "tuxedo t-";

                } else {
                    mans.log(";n;grhuh?");

                }

                audio.zipup.load();
                audio.zipup.play();

                setTimeout(dresser, 400);

            } else if (mem.substate == "dresser pants") {

                if (val.charAt(0) == "z") {
                    pl.inv.pants = "zipoff";

                } else if (val.charAt(0) == "j") {
                    pl.inv.pants = "jorts";

                } else if (val.charAt(0) == "l") {
                    pl.inv.pants = "leggings";

                } else if (val.charAt(0) == "s") {
                    pl.inv.pants = "swim trunks";

                } else {
                    mans.log(";n;gr?");

                }

                audio.zipup.load();
                audio.zipup.play();

                setTimeout(dresser, 400);

            } else {

                if (val.charAt(0) == "r") {
                    record();

                } else if (val.charAt(0) == "l") {
                    lockbox();

                } else if (val.charAt(0) == "d") {
                    dresser();

                } else if (val.charAt(0) == "h") {
                    mem.state = "intro-hallway";
                    hallway("print hallway intro");
                }

            }

        }
    }

    return main
}();

module.exports = main