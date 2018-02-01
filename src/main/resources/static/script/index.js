$(function() {

    $.ajax({
        type: "GET",
        url: "/getchannels",
        success: response => {
            populateChannelList(response.channels)
        }
    })


    $('#createChannelButton').on("click", function(event){
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/newchannel",
            data: $('#newChannel').serialize(),
            success: response => {
                populateChannelList(response.channels);
            },
            error: response => {
                console.log(response);
            }

        });

    });

    function populateChannelList(channels){
        $('#channelList').empty();
            channels.forEach(function (element){
            let channelButton = $('<button/>', {}).attr("data-id",element.id).text(element.name);
            channelButton.click(loadChannelMessages);
            $('#channelList').append(channelButton);
        })
    }

    function loadChannelMessages(){
       let channelId = $(this).attr("data-id");
       $.ajax({
           type: "GET",
           url: "/channel/" + channelId,
           success: response => {
                console.log(response.channelMessages)
           }
       })
    }
})





