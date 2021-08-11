$(document).ready(function () {
    $("#products").on("click", "#linkAddProduct", function (e) {
        e.preventDefault();
        let link = $(this);
        let url = link.attr("href");

        $("#addProductModal").on("shown.bs.modal", function () {
            $(this).find("iframe").attr("src", url);
        });

        $("#addProductModal").modal();
    });
});

function addProduct(productId, productName) {
    getShippingCost(productId);
}

function getShippingCost(productId) {
    let selectedCountry = $("#country option:selected");
    let countryId = selectedCountry.val();

    let state = $("#state").val();
    if (state.length == 0) {
        state = $("#city").val();
    }

    let requestUrl = contextPath + "get_shipping_cost";
    let params = {productId: productId, countryId: countryId, state: state};

    $.ajax({
        type: 'POST',
        url: requestUrl,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: params
    }).done(function (shippingCost) {
        getProductInfo(productId);
    }).fail(function (err) {
        showWarningModal(err.responseJSON.message);
        getProductInfo(productId);
    }).always(function () {
        $("#addProductModal").modal("hide");
    });
}

function getProductInfo(productId){
    let requestURL = contextPath + "products/get/" + productId;

    $.get(requestURL, function (productJson){
        console.log(productJson);
    }).fail(function(err){
        showWarningModal(err.responseJSON.message);
    });
}

function isProductAlreadyAdded(productId) {
    let productExists = false;

    $(".hiddenProductId").each(function (e) {
        let aProductId = $(this).val();

        if (aProductId == productId) {
            productExists = true;
            return;
        }

    });

    return productExists;
}