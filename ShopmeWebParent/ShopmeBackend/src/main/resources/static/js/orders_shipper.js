var confirmText;
var confirmModalDialog;
var yesButton;

$(document).ready(function () {
    confirmText = $("#confirmText");
    confirmModalDialog = $("#confirmModal");
    yesButton = $("#yesButton");
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
        console.log(response);
    }).fail(function (err) {

    });
}

function showUpdateConfirmModal(link) {
    let orderId = link.attr("orderId");
    let status = link.attr("status");

    yesButton.attr("href", link.attr("href"));

    confirmText.text("Are you sure you want to update status of the order ID #" + orderId + " to " + status + "?");
    confirmModalDialog.modal();
}