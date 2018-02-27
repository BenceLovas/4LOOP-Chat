$(function () {

    function postRegistrationData(event) {
        let validInput = true;
        $.each($('#registrationForm').serializeArray(), function (index, inputField) {
            if(inputField.value == "" && validInput == true){
                $("#loginError").html("&nbsp;");
                $("#registrationError").html(inputField.name + " is empty");
                $("#registrationForm :Password").val("");
                validInput = false;
            }
        });

        event.preventDefault(event);
        if(validInput == true) {
            $.ajax({
                type: "POST",
                url: "/user/registration",
                data: $('#registrationForm').serialize(),
                success: response => {
                    window.location.replace(response.redirect);
                },
                error: response => {
                    $("#loginError").html("&nbsp;");
                    $("#registrationError :Password").val("");
                    $("#registrationError").text(response.responseJSON.response);
                }
            });
        }
    }

    $('#registrationButton').click(postRegistrationData);



    function postLoginData(event) {

        let validInput = true;
        $.each($('#loginForm').serializeArray(), function (index, inputField) {
            if(inputField.value == "" && validInput == true){
                $("#registrationError").html("&nbsp;");
                $("#loginError").html(inputField.name + " is empty");
                $('#loginForm :Password').val("");

                validInput = false;
           }
        });

        event.preventDefault(event);
        if(validInput == true) {
            $.ajax({
                type: "POST",
                url: "/user/login",
                data: $('#loginForm').serialize(),
                success: response => {
                    window.location.replace(response.redirect);
                },
                error: response => {
                    $("#registrationError").html("&nbsp;");
                    $("#loginError").text(response.responseJSON.response);
                    $('#loginForm :Password').val("");
                }
            });
        }
    }

    $('#loginButton').click(postLoginData);

});