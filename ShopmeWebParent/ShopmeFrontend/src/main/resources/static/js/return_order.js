const returnModal = $("#returnOrderModal");
const modalTitle = $("#returnOrderModalTitle");
const fieldNote = $("#returnNote");
const firstButton = $("#firstButton");
const secondButton = $("#secondButton");
const divReason = $("#divReason");
const divMessage = $("#divMessage");
var orderId;

$(document).ready(function (){
    handleReturnOrderLink();
});

function showReturnModalDialog(link){
    divMessage.hide();
    divReason.show();
    firstButton.show();
    secondButton.text("Cancel");
    fieldNote.val("");

    orderId = link.attr("orderId");
    modalTitle.text("Return Order ID #" + orderId);
    returnModal.modal("show");
}

function showMessageModal(message){
    divReason.hide();
    firstButton.hide();
    secondButton.text("Close");
    divMessage.text(message);

    divMessage.show();
}

function handleReturnOrderLink() {
    $(".linkReturnOrder").on("click", function(e) {
        e.preventDefault();
        showReturnModalDialog($(this));
    });
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
        showMessageModal("Return request has been sent");
    }).fail(function(err) {
        console.log(err);
        showMessageModal(err.responseText);
    });

}