$(document).ready(function (){
    $("#productList").on("click", ".linkRemove", function (e){
        e.preventDefault();

        if(doesOrderHaveOnlyOneProduct()){
            showWarningModal("Could not remove product. The order must have at least one product.");
        } else {
            removeProduct($(this));
            updateOrderAmounts();
        }
    });
});

function removeProduct(link) {
    let rowNumber = link.attr("rowNumber");
    $("#row" + rowNumber).remove();
    $("#blankLine" + rowNumber).remove();

    $(".divCount").each(function(index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function doesOrderHaveOnlyOneProduct(){
    let productCount = $(".hiddenProductId").length;
    return productCount == 1;
}