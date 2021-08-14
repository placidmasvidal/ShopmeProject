$(document).ready(function (){
    $("#productList").on("click", ".linkRemove", function (e){
        e.preventDefault();
        removeProduct($(this));
    });
});

function removeProduct(link) {
    let rowNumber = link.attr("rowNumber");
    $("#row" + rowNumber).remove();

    $(".divCount").each(function(index, element) {
        element.innerHTML = "" + (index + 1);
    });
}