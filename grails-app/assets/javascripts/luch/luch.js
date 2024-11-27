$(document).bind("mobileinit", function(){
    $.mobile.ignoreContentEnabled = true;
    $.mobile.ajaxEnabled = false;
});
$(document).ready(function() {
    // disable ajax nav
    $.mobile.ignoreContentEnabled = true;
    $.mobile.ajaxLinksEnabled = false;
});