$(function () {

    function postRegistration(event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/registration",
            data: $('#registrationForm').serialize(),
            success: response => {
                console.log(response);
            },
            error: response => {
                console.log(response.responseJSON);
            }
        })
    }

    $('#registrationButton').click(postRegistration);

});