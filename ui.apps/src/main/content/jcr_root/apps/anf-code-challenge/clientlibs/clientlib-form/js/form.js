(function() {
    $('#error-submission').hide();
    $('#successful-form-submission').hide();
    $("form").submit(function(event) {
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
            success: function(data) {
                if (data && data.localeCompare("Successful") == 0) {
                    $('#error-submission').hide();
                    $('#successful-form-submission').show();
                } else {
                    $('#error-submission').show();
                    $('#successful-form-submission').hide();
                }
            }
        });
        event.preventDefault();
    });
})();