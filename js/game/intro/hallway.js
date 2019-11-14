// hallway object
var hallway = function() {

    var doorCounter = 0;

    function doors(whichDoor) {

        mem.substate = "doors";
        var text = [
            ";nThe clock on the wall reads 3:42AM. Paintings are hastily hung all over the wall. Photos of yourself, your friends, roommates, and hand done paintings you had finished years ago. They were getting old and dusty. Your roommates had put out a old tv which stood acute in the corner, not even plugged in. Three doors to the left facing the exit to the living room. It might be worth knocking, that clock can't be right anyway.",
            ";n    1: Knock on Nelson’s Door. A sign reads to stay quiet.",
            ";nNo answer. Nelson likes to keep to himself in the best of times, usually locking himself in his room for days upon end. Can’t say I’m too surprised.",
            ";n    2: Knock on Cale’s Door.",
            ";nNo answer. If cale wasn’t up already there’s no chance he’d be woken up. The guy sleeps like a rock as soon as he starts but has a lot of trouble getting to that point. I had a slight hope.",
            ";n    3: Knock on Ralph’s Door.",
            ";nNo answer. One of the more straightforward of the bunch, Ralph probably wouldn’t be up to these shenanigans in the first place.",
            ";nThey must be in the living room."
        ];
        var intro = function() {
            mans.term.space();
            //list all doors for player after intro message.
            mansole.term.log(text[0]);
            mansole.term.log(text[1], true);
            mansole.term.log(text[3], true);
            mansole.term.log(text[5], true);
        };
        var nelson = function() {
            mansole.term.log(text[2]);
            doorCounter++;
        };
        var cale = function() {
            mansole.term.log(text[4]);
            doorCounter++;
        };
        var ralph = function() {
            mansole.term.log(text[6]);
            doorCounter++;
        };
        if (whichDoor == 0)
            nelson();
        else
        if (whichDoor == 1)
            cale();
        else
        if (whichDoor == 2)
            ralph();
        else
            intro();

        if (doorCounter >= 3) {
            mansole.term.log(text[7]);
            //move on to living room.
            mem.substate = "";
        }
    }

    function commands(val) {

        if (mem.state == "intro-hallway") {

            if (mem.substate == "doors") {

                if (val.charAt(0) == "1" || val.charAt(0) == "n")
                    doors(0)
                else
                if (val.charAt(0) == "2" || val.charAt(0) == "c")
                    doors(1)
                else
                if (val.charAt(0) == "3" || val.charAt(0) == "r")
                    doors(2)

            } else {

                if (val.includes("print hallway intro"))
                    doors()
                else
                if (val.charAt(0) == "l") {
                    mem.state = "intro-living-room"
                    livingroom("print living room intro");
                }

            }

        }
    }

    return commands
}();