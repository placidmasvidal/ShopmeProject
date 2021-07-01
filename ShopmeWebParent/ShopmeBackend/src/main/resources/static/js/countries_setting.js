var buttonLoad;
var dropDownCountry;

$(document).ready(function (){
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");
   
   buttonLoad.click(function (){
       loadCountries();
   });
});

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJson){
        dropDownCountry.empty();

        $.each(responseJson, function (index, country){
           optionValue = country.id + "-" + country.code;
           $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function (){
        buttonLoad.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error.");
    });
}

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}