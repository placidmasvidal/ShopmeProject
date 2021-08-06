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

    $("#productList").on("change", ".quantity-input", function (e){
        updateSubtotalWhenQuantityChanged($(this));
    });

    $("#productList").on("change", ".price-input", function (e){
        updateSubtotalWhenPriceChanged($(this));
    });
});

function setAndFormatNumberForField(fieldId, fieldValue){
    let formattedValue = $.number(fieldValue, 2);
    $("#" + fieldId).val(formattedValue);
}

function getNumberValueRemovedThousandSeparator(fieldRef){
    let fieldValue = fieldRef.val().replace(",", "");
    return parseFloat(fieldValue);
}

function updateSubtotalWhenPriceChanged(input) {
    let priceValue = getNumberValueRemovedThousandSeparator(input);
    let rowNumber = input.attr("rowNumber");
    let quantityField = $("#quantity" + rowNumber);
    let quantityValue = quantityField.val();
    let newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

function updateSubtotalWhenQuantityChanged(input) {
    let quantityValue = input.val();
    let rowNumber = input.attr("rowNumber");
    let priceValue = getNumberValueRemovedThousandSeparator($("#price" + rowNumber));
    let newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

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