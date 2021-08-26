const returnModal = $("#returnOrderModal");
const modalTitle = $("#returnOrderModalTitle");
const fieldNote = $("#returnNote");
var orderId;

$(document).ready(function (){
    $(".linkReturnOrder").on("click", function (e){
       e.preventDefault();
       handleReturnOrderLink($(this));
    });
});

function handleReturnOrderLink(link) {
    orderId = link.attr("orderId");
    returnModal.modal("show");
    modalTitle.text("Return Order ID #" + orderId);
}

function submitReturnOrderForm(){
    let reason = $("input[name='returnReason']:checked").val();
    let note = fieldNote.val();

    sendReturnOrderRequest(reason, note);

    return false;
}

function sendReturnOrderRequest(reason, note) {
    let requestURL = contextPath + "orders/return";
    let requestBody = {orderId: orderId, reason: reason, note: note};

    $.ajax({
        type: "POST",
        url: requestURL,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(requestBody),
        contentType: 'application/json'

    }).done(function(returnResponse) {
        console.log(returnResponse);
    }).fail(function(err) {
        console.log(err);
    });

}