var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function () {
    confirmText = $("#confirmText");
    confirmModalDialog = $("#confirmModal");
    yesButton = $("#yesButton");
    noButton = $("#noButton");

    $(".linkUpdateStatus").on("click", function (e){
        e.preventDefault();
        let link = $(this);
        showUpdateConfirmModal(link);
    });

    addEventHandlerForYesButton();
});

function addEventHandlerForYesButton() {
    yesButton.click(function (e){
       e.preventDefault();
       sendRequestToUpdateOrderStatus($(this));
    });
}

function sendRequestToUpdateOrderStatus(button){
    let requestUrl = button.attr("href");

    $.ajax({
        type: 'POST',
        url: requestUrl,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        showMessageModal("Order updated successfully");
        console.log(response);
    }).fail(function (err) {
        showMessageModal("Error updating order status");
    });
}

function showUpdateConfirmModal(link) {
    noButton.text("NO");
    yesButton.show();

    let orderId = link.attr("orderId");
    let status = link.attr("status");

    yesButton.attr("href", link.attr("href"));

    confirmText.text("Are you sure you want to update status of the order ID #" + orderId + " to " + status + "?");
    confirmModalDialog.modal();
}

function showMessageModal(message){
    noButton.text("Close");
    yesButton.hide();

    confirmText.text(message);
}