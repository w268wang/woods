// Wood Index

var topMenu = $("#top-nav");
var topMenuHeight = topMenu.outerHeight();
// All list items
var navItems = $("#top-nav-left").find("a");
// Anchors corresponding to nav items
var scrollItems = navItems.map(function(){
    var item = $($(this).attr("href"));
    if (item.length) {
        return item;
    }
});

// Bind click handler to menu items
// so we can get a fancy scroll animation
navItems.click(function(e){
    var href = $(this).attr("href"),
    offsetTop = href === "#" ? 0 : $(href).offset().top;
    $('html, body').stop().animate({ 
        scrollTop: offsetTop
    }, 800, "swing");
    e.preventDefault();
});

// Bind to scroll
$(window).scroll(function(){
    // Get container scroll position
    var fromTop = $(this).scrollTop()+topMenuHeight;
    var windowBottom = $(this).scrollTop()+window.innerHeight;

    // Get id of current scroll item
    var cur = scrollItems.map(function(){
        if ($(this).offset().top < fromTop)
            return this;
    });
    // Get the id of the current element
    cur = cur[cur.length-1];
    var id = cur && cur.length ? cur[0].id : "";

    console.log(windowBottom+" , "+document.body.scrollHeight);
    //console.log(lastid+" , "+id);
    if (windowBottom == document.body.scrollHeight) {
        navItems
        .parent().removeClass("active")
        .end();
        $(navItems[navItems.length-1]).parent().addClass("active");
    } else {
        navItems
        .parent().removeClass("active")
        .end().filter("[href=#"+id+"]").parent().addClass("active");
    }
});

function layout() {
    $('.container').css('width', $(window).width());
}

$(function() {
    var windowWidth = $(window).width();
    $('.section-container').css('width', windowWidth);
});