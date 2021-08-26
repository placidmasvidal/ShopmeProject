const returnModal = $("#returnOrderModal");
const modalTitle = $("#returnOrderModalTitle");
const fieldNote = $("#returnNote");

$(document).ready(function (){
    $(".linkReturnOrder").on("click", function (e){
       e.preventDefault();
       handleReturnOrderLink($(this));
    });
});

function handleReturnOrderLink(link) {
    let orderId = link.attr("orderId");
    returnModal.modal("show");
    modalTitle.text("Return Order ID #" + orderId);
}

function submitReturnOrderForm(){
    let reason = $("input[name='returnReason']:checked").val();
    let note = fieldNote.val();
    alert(reason + " - " + note);
}