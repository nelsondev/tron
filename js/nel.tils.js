var nel_tils = function() {

    //
    // event and event handling class
    //
    var listener = function() {

        function registerListener(obj, events, callback, ...args) {
            var arr = events.split(" ");
            for (var i in arr) {
                obj.addEventListener(arr[i], () => callback(...args));
            }
        }

        return {
            add: registerListener,
        }
    }();

    // 
    // object manipulation class
    //
    var objects = function() {

        function getObjectByName(str) {
            return document.querySelector(str);
        }

        return {
            getObjectByName: getObjectByName,
        }
    }();

    //
    // html manipulation class
    //
    var html = function() {

        function sanitize(str) {
            var tempObj = document.createElement("div");
            tempObj.textContent = str;
            return tempObj.innerHTML;
        }

        function trim(element) {
            var obj = objects.elem("");
        }

        return {
            sanitize: sanitize,
            trim: trim,
        }
    }();

    //
    // returns for the nel_tils class
    //
    return {
        listener: listener,
        objects: objects,
        html: html,
    }
}();

module.exports = {
    listen: nel_tils.listener.add,
    sanitizeHtml: nel_tils.html.sanitize,
    trimHtml: nel_tils.html.trim,
    element: nel_tils.objects.getObjectByName,
}