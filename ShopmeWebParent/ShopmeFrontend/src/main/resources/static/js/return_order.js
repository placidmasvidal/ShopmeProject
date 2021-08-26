const returnModal = $("#returnOrderModal");
const modalTitle = $("#returnOrderModalTitle");

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