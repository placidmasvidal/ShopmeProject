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
        updateOrderAmounts();
    });

    $("#productList").on("change", ".price-input", function (e){
        updateSubtotalWhenPriceChanged($(this));
        updateOrderAmounts();
    });

    $("#productList").on("change", ".cost-input", function (e){
        updateSubtotalWhenPriceChanged($(this));
        updateOrderAmounts();
    });

    $("#productList").on("change", ".ship-input", function (e){
        updateSubtotalWhenPriceChanged($(this));
        updateOrderAmounts();
    });
});

function updateOrderAmounts() {
    let totalCost = 0.0;

    $(".cost-input").each(function (e){
        let costInputField = $(this);
        let rowNumber = costInputField.attr("rowNumber");
        let quantityValue = $("#quantity" + rowNumber).val();

        let productCost = getNumberValueRemovedThousandSeparator(costInputField);
        totalCost += productCost * parseInt(quantityValue);
    });

    setAndFormatNumberForField("productCost", totalCost);

    let orderSubtotal = 0.0;

    $(".subtotal-output").each(function (e){
        let productSubtotal = getNumberValueRemovedThousandSeparator($(this));
        orderSubtotal += productSubtotal;
    });

    setAndFormatNumberForField("subtotal", orderSubtotal);

    let shippingCost = 0.0;

    $(".ship-input").each(function (e){
        let productShip = getNumberValueRemovedThousandSeparator($(this));
        shippingCost += productShip;
    });

    setAndFormatNumberForField("shippingCost", shippingCost);

    let tax = getNumberValueRemovedThousandSeparator(fieldTax);

    let orderTotal = orderSubtotal + tax + shippingCost;

    setAndFormatNumberForField("total", orderTotal);
}

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

function processFormBeforeSubmit(){

    setCountryName();

    removeThousandSeparatorForField(fieldProductCost);
    removeThousandSeparatorForField(fieldSubtotal);
    removeThousandSeparatorForField(fieldShippingCost);
    removeThousandSeparatorForField(fieldTax);
    removeThousandSeparatorForField(fieldTotal);

    $(".cost-input").each(function (e){
        removeThousandSeparatorForField($(this));
    });

    $(".price-input").each(function (e){
        removeThousandSeparatorForField($(this));
    });

    $(".subtotal-output").each(function (e){
        removeThousandSeparatorForField($(this));
    });

    $(".ship-input").each(function (e){
        removeThousandSeparatorForField($(this));
    });

}

function removeThousandSeparatorForField(fieldRef){
    fieldRef.val(fieldRef.val().replace(",", ""));
}

function setCountryName(){
    let selectedCountry = $("#country option:selected");
    let countryName = selectedCountry.text();
    $("#countryName").val(countryName);
}