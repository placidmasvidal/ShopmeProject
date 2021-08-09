$(document).ready(function (){
    $("#products").on("click", "#linkAddProduct", function (e){
        e.preventDefault();
        let link = $(this);
        let url = link.attr("href");

        $("#addProductModal").on("shown.bs.modal", function (){
           $(this).find("iframe").attr("src", url);
        });

        $("#addProductModal").modal();
    });
});