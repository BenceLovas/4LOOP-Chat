$(function () {

    function postRegistrationData(event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/user/registration",
            data: $('#registrationForm').serialize(),
            success: response => {
                window.location.replace(response.redirect);
            },
            error: response => {
                $('#registrationError').text(response.responseJSON.response);
            }
        });
    }

    $('#registrationButton').click(postRegistrationData);

    function postLoginData(event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/user/login",
            data: $('#loginForm').serialize(),
            success: response => {
                window.location.replace(response.redirect);
            },
            error: response => {
                $('#loginError').text(response.responseJSON.response);
            }
        });
    }

    $('#loginButton').click(postLoginData);

});