$(function() {
    
    function getUsers() {
        $.ajax({
            type: "GET",
            url: "/users",
            success: data => {
                let div = $('<div/>', {});
                data.forEach((user) => {
                    div.append($('<p/>').text(user.id + " " + user.name))
                });
                $('#usersWrapper').append(div);
            }
        })
    }

    $('#getUsersButton').click(getUsers);
    
});
