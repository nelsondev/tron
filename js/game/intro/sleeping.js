var sleeping = function(memory) {

    var nt = require('/home/nelso/Documents/console emu/js/nel.tils.js'),
        mans = require("/home/nelso/Documents/console emu/js/mansole.js"),
        mansole = mans,
        manny = require("/home/nelso/Documents/console emu/js/manny.js"),
        mem = memory,
        player = manny.player,
        pl = player;

    function print() {

    	console.log(memory);

    	console.log(mem);

        // let text = [
        //     ';nA man sleeps in a dark dim room, only bright from the moons shine; awake, listening to the thunder and lightning passing. Sleepless nights aren’t called upon but brought upon by the human mind for reasons unknown.',
        //     ';nBANG BANG!',
        //     ';n   The door to the room shutters as if a large gust of wind had blown it off the hinges. Throwing yourself out of bed in defence. Your eyes adjust to the dim light only able to make out vague forming blurs of dark static.',
        //     ';nCLICK!',
        //     ';nThe lamp illuminates the room only not reaching to the deep corners. Dark shadows start taking shape: a dresser, a record player, and your lockbox on top of your shelf. It looks safe.'
        // ];

        // mem.state = "intro";

        // audio.title.pause();

        // mans.term.space();
        // mans.log(text);

    }

    function main(val) {
    	print();
    }

    return main

}();

module.exports = sleeping