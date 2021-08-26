var returnModal;

$(document).ready(function (){
    returnModal = $("#returnOrderModal");

    $(".linkReturnOrder").on("click", function (e){
       e.preventDefault();
       handleReturnOrderLink();
    });
});

function handleReturnOrderLink() {
    returnModal.modal("show");
}