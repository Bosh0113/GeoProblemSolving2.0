$(document).ready(function () {
    $(".subNavbar").hide();
    $(".navName").click(function(){
        $(this).siblings(".subNavbar").toggle();
    });
    $(".modelPanel .info .row").eq(0).show();
    $(".modelPanel .info .row").eq(1).show();
    $(".modelPanel .expandBtn").html("Expand&nbsp; <i class=\"fa fa-caret-down\"></i>");

    $(".expandBtn").click(function () {
        if($(this).text().indexOf("Expand")>=0){
            $(this).parent().siblings(".info").children(".row").show();
            $(this).html("Collapse&nbsp; <i class=\"fa fa-caret-up\"></i>");
        }else{
            $(this).parent().siblings(".info").children(".row").hide();
            $(this).parent().siblings(".info").children(".row").eq(0).show();
            $(this).parent().siblings(".info").children(".row").eq(1).show();
            $(this).html("Expand&nbsp; <i class=\"fa fa-caret-down\"></i>");
        }
    });

    $(".navbarPanel .navigation a").click(function () {
        var index = $(this).attr("index")*1;
        $(".infoPanel").hide();
        $(".infoPanel").eq(index).show();
        if($(".infoPanel").eq(index).children(".info").children(".row").length<3){
            $(".infoPanel").eq(index).children(".expand").hide();
        }
        $(".infoPanel").eq(index).children(".info").children(".row").eq(0).show();
        $(".infoPanel").eq(index).children(".info").children(".row").eq(1).show();
    });

    var height = $(window).height();
    var minH = height-60;
    $(".infoPanel").css("min-height",minH+"px");

    window.onresize = function () {
        var height = $(window).height();
        var minH = height-60;
        $(".infoPanel").css("min-height",minH+"px");
    };
});
