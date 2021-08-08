$(document).ready(function (){
    $("#products").on("click", "#linkAddProduct", function (e){
        e.preventDefault();
        $("#addProductModal").modal();
    });
});