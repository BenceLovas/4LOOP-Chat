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
function getUser1() {
    $.ajax({
        type: "GET",
        url: "/getUser1",
        data:data,
        success: console.log(data)
    })
}
