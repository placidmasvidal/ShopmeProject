$(document).ready(function (){
    $(".linkMinus").on("click", function (evt){
        evt.preventDefault();
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function (evt){
        evt.preventDefault();
        increaseQuantity($(this));
    });
});

function decreaseQuantity(link){
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) -1;

    if(newQuantity > 0){
        quantityInput.val(newQuantity);
    } else {
        showWarningModal('Minimum quantity is 1');
    }
}

function increaseQuantity(link){
    let productId = link.attr("pid");
    let quantityInput = $("#quantity" + productId);
    let newQuantity = parseInt(quantityInput.val()) +1;

    if(newQuantity <= 5){
        quantityInput.val(newQuantity);
    } else {
        showWarningModal('Maximum quantity is 5');
    }
}