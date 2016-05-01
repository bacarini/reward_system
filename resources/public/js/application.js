$(function(){
    var url = window.location;
    // Will only work if string in href matches with location
    $('ul.nav li').removeClass("active")
    $('ul.nav a[href="'+ url.pathname +'"]').parent().addClass('active');
});
