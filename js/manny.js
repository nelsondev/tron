"use strict";

//
// Welcome to Manny's Week . .
//

/*
   Built on a little electron module I made named mansole.js.
   Manny's Week is a text based adventure remembering a full
   game D.L.A had made a while back just sitting on a couch
   in the game engine construct 2.
*/

/*
   I basically had been looking for a reason to be a nerd again.
   It sparked my interest in programming through use of that
   engine so much that it pushed me to learn javascript as a
   language. This is my first text based adventure I've ever
   made. Give it a break, and check out my unorganized brain
   trying to give organized code a try.
*/

var mannys_week = function() {

    // Imports
    var nt = require('./nel.tils.js'),
        mans = require("./mansole.js"),
        mansole = mans;

    // Game Memory object.
    var memory = {
            state: "",
            substate: "",
            volume: 1,
        },
        mem = memory;

    // Player object.
    var player = {
            inv: {
                shirt: "",
                pants: "",
            }
        },
        pl = player;

    // Add item to players inventory and play sound
    player.addItem = function(str, amount) {
            audio.orb.volume = mem.volume / 10;
            audio.orb.load();
            audio.orb.play();
            player.inv[str] = (amount) ? amount : 1;
        },
        player.add = player.addItem;

    //
    // Game engine shell function
    //
    var shell = function() {

        function main(val) {
            // change volume of all audio elements.
            if (val.includes("vol")) {
                audio.setVolume(val);
            }

            if (val.includes("?")) {
                mans.log(mem.state);
            }

            // print title screen if play is typed.
            if (!mem.state) {

                if (val.includes("manny")) {
                    title("print titlescreen");
                }

            }
        }

        return main

    }();

    //
    // Titlescreen game function
    //
    var title = function() {

        function print() {

            var title = [
                '. . . . . . . . . .',
                '.####@@@@@###  #@@.',
                '.   #######     ##.',
                '.                 .',
                ';b.  .########      .',
                ';b. ..## ## ##      .',
                ';b. ..########      .',
                ';b. ..########      .',
                ';b.    .####        .',
                ';b.   ..####        .',
                ';b.   ..####        .',
                ';b.   ..####        .',
                ';b.   ..#..#        .',
                ';b.   ..#..#        .',
                ';b.   ..#..#        .',
                ';b... ..#..#        .',
                ';b. ..###..### ......',
                '. @ @@ @@@@@@@@@@ .',
                "🄼🄰🄽🄽🅈'🅂 🅆🄴🄴🄺 ",
                '. . . . . . . . . .',
                'type start'
            ];

            mem.state = "title";

            audio.title.play();

            mans.term.space();
            mans.log(title, true);

        }


        function main(val) {
            if (val == "print titlescreen")
                print();

            if (mem.state == "title") {

                if (val.includes("start")) {
                    // intro("print");
                }

            }
        }

        return main

    }();

    //
    // Intro game function
    //
    var intro = function() {

        var sleeping = require("./game/intro/sleeping.js");
        // var room = require("./js/game/intro/room.js");
        // var hallway = require("./js/game/intro/hallway.js");
        // var livingroom = require("./js/game/intro/living.room.js");

        function main(val) {
            sleeping(val);
            // room();
            // hallway();
            // livingroom();
        }

        return main
    }();

    //
    // Define audio elements
    //
    var audio = {
        setVolume: function(str) {
            // get all audio elements
            var aud = document.getElementsByTagName("audio");
            // get argument 1;
            var vol = str.split(" ")[1];
            // loop through all audio elements
            for (var i in aud) {
                // set volume on audio elements to number if, else 0.
                aud[i].volume = (vol >= 0 && vol <= 1) ? vol : 0;
            }
            if (vol >= 0 && vol <= 1)
                mem.volume = vol;
            audio.volume.load();
            audio.volume.play();
        },
        title: nt.element("#song-titlescreen"),
        title_boosted: nt.element("#song-titlescreen"),
        door_close: nt.element("#sound-door-close"),
        door_knock: nt.element("#sound-door-knock"),
        invalid: nt.element("#sound-invalid"),
        machine_noise: nt.element("#sound-machine-noise"),
        rain: nt.element("#sound-rain"),
        rain_thunder: nt.element("#sound-rain-thunder"),
        thunder_1: nt.element("#sound-thunder1"),
        thunder_2: nt.element("#sound-thunder2"),
        volume: nt.element("#sound-volume"),
        zipup: nt.element("#sound-zipup"),
        orb: nt.element("#sound-orb"),
    };

    //
    // Main game function 
    //
    function main() {
        // Mansole main function. Runs all code on the "enter sent", event.
        mans.run(function(val) {

            // log player input to show what they typed.
            mans.log("&gt; " + val);

            // shell commands
            shell(val);

            // intro commands
            intro(val);
        });
    }

    //
    // Function Exports for Manny.js
    //
    return {
        main: main,
        memory: memory,
        mem: memory,
        player: player,
        pl: player,
        audio: audio,
    }
}();

// NodeJS file Exports for Manny.js
module.exports = {
    main: mannys_week.main,
    memory: mannys_week.memory,
    mem: mannys_week.memory,
    player: mannys_week.player,
    pl: mannys_week.player,
    audio: mannys_week.audio,
}