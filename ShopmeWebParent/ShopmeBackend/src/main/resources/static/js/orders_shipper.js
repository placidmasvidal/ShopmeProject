$(document).ready(function () {
    $(".linkUpdateStatus").on("click", function (e){
        e.preventDefault();
        alert($(this).attr("href"));
    });
});