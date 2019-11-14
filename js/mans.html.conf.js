let config = {
    "linebreak": {
        "<br/>": /;n/g
    },
    "underline": {
        "<span class='txt-ul'>": /;ul/g,
        "</span>": /ul;/g
    },
    "white": {
        "<span class='text-white'>": /;w/g,
        "</span>": /w;/g
    },
    "gray": {
        "<span class='text-gray'>": /;gr/g,
        "</span>": /gr;/g
    },
    "red": {
        "<span class='text-red'>": /;r/g,
        "</span>": /r;/g
    },
    "blue": {
        "<span class='text-blue'>": /;b/g,
        "</span>": /b;/g
    },
    "lightblue": {
        "<span class='text-light-blue'>": /;lb/g,
        "</span>": /lb;/g
    },
    "purple": {
        "<span sclass='text-purple'": /;pr/g,
        "</span>": /pr;/g
    },
    "green": {
        "<span class='text-green'": /;g/g,
        "</span>": /g;/g
    },
    "orange": {
        "<span sclass='text-orange'>": /;o/g,
        "</span>": /o;/g
    }
};

module.exports = config;