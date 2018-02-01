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
            let channelButton = $('<button/>', {}).attr("data-id",element.id).text(element.name).addClass("channelButton");

            channelButton.click(loadChannelMessages);
            $('#channelList').append(channelButton);;
            $('#channelList').append("<br>")

        })
    }

    function loadChannelMessages(){
       console.log("Loeadig messages");
       $("#channelWindow").html("");
       let channelId = $(this).attr("data-id");
       $.ajax({
           type: "GET",
           url: "/channel/" + channelId,
           success: response => {
                console.log(response.channelMessages)
                let channelMessagesDiv = $("<div>", {
                id: "channelMessagesDiv"});
                $("#channelWindow").append(channelMessagesDiv);
                response.channelMessages.forEach(function (element){
                    let div = $("<div/>", {});
                    let date = $("<p/>").text(timeConverter(element.date));
                    let author = $("<p/>").text(element.author.name);
                    let message = $("<p/>").text(element.message);
                    div.append(date).append(author).append(message);
                    $("#channelMessagesDiv").append(div);
                })
                let texterdiv = $("<div>");
                let messageInput = $("<input/>", {
                    id: "messageInput",
                    type: "text",
                    placeholder: "Write message here...",
                    name: "message"
                });
                let sendMessageButton = $("<button/>", {
                    type: "submit"
                }).text("Send");
                sendMessageButton.click(function () {
                    let message = $("#messageInput").val();
                    let data = {"message": message, "channelId": channelId};
                    //console.log(channelId);
                    $.ajax({
                        type: "POST",
                        url: "/channel/" + channelId + "/newmessage",
                        data: data,
                        success: response => {
                        $("#channelMessagesDiv").html("");
                            response.channelMessages.forEach(function (element){
                                let div = $("<div/>", {});
                                let date = $("<p/>").text(timeConverter(element.date));
                                let author = $("<p/>").text(element.author.name);
                                let message = $("<p/>").text(element.message);
                                div.append(date).append(author).append(message);
                                $("#channelMessagesDiv").append(div);
                            })

                        }
                    })
                });
                $("#channelWindow").prepend(sendMessageButton);
                $("#channelWindow").prepend(messageInput);
           }
       })
    }

    function timeConverter(UNIX_timestamp){
        var a = new Date(UNIX_timestamp);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        if (min.toString().length < 2){
            var time = date + ' ' + month + ' ' + year + ' ' + hour + ':0' + min;
        } else {
            var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min;
        }
        return time;
    }




})





