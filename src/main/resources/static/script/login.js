$(function () {

    function postRegistration(event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/registration",
            data: $('#registrationForm').serialize(),
            success: response => {
                console.log(response);
                console.log("lali");
            },
            error: response => {
                console.log("wut");
                console.log(response);
            }
        })
    }

    $('#registrationButton').click(postRegistration);

});