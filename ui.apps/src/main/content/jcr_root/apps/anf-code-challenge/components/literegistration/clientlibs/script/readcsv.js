$(document).ready(function () {

	var elements = document.getElementById("csvfilePath");
    if(elements != null){
        var dropDownPath = elements.getAttribute("data-dropDownPath");
        d3.csv(dropDownPath, function (data) {
            for (var i = 0; i < data.length; i++) {
                $("#" + $("select").attr("id")).append($("<option>").val(data[i].code).text(data[i].value));
            }
        });
    }


});

