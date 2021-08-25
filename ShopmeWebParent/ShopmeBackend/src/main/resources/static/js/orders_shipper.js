var confirmText;
var confirmModalDialog;

$(document).ready(function () {
    confirmText = $("#confirmText");
    confirmModalDialog = $("#confirmModal");
    $(".linkUpdateStatus").on("click", function (e){
        e.preventDefault();
        let link = $(this);
        showUpdateConfirmModal(link);
    });
});

function showUpdateConfirmModal(link) {
    let orderId = link.attr("orderId");
    let status = link.attr("status");

    confirmText.text("Are you sure you want to update status of the order ID #" + orderId + " to " + status + "?");
    confirmModalDialog.modal();
}