(function(){
  $("form").submit(function (event) {
    var formData = {
      firstName: $('input[name=firstName]').val(),
      lastName: $('input[name=lastName]').val(),
      age: $('input[name=age]').val(),
      country: $('input[name=country]').val(),
    };

    $.ajax({
      type: "POST",
      url: "/bin/saveUserDetails",
      data: formData,
      success: function(json){
        console.log(json);
      }
});

      event.preventDefault();
  });
})();