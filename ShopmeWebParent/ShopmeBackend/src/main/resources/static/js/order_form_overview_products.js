var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function (){

    fieldProductCost = $("#productCost");
    fieldSubtotal = $("#subtotal");
    fieldShippingCost = $("#shippingCost");
    fieldTax = $("#tax");
    fieldTotal = $("#total");

    formatOrderAmounts();
    formatProductAmounts();

});

function formatProductAmounts(){
    $(".cost-input").each(function (e) {
        formatNumberForField($(this));
    });

    $(".price-input").each(function (e) {
        formatNumberForField($(this));
    });

    $(".subtotal-output").each(function (e) {
        formatNumberForField($(this));
    });

    $(".ship-input").each(function (e) {
        formatNumberForField($(this));
    });
}

function formatOrderAmounts(){
    formatNumberForField(fieldProductCost);
    formatNumberForField(fieldSubtotal);
    formatNumberForField(fieldShippingCost);
    formatNumberForField(fieldTax);
    formatNumberForField(fieldTotal);
}

function formatNumberForField(fieldRef){
    fieldRef.val($.number(fieldRef.val(), 2));
}