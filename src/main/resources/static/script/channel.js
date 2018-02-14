var channelController = {
    loadChannelController : function (){
        let addNewChannel = $('<form/>', {});
        addNewChannel.attr("action", "#");
        addNewChannel.attr("id", "newChannel");
        let inputField = $('<input/>', {});
        inputField.attr("name","channelName");
        inputField.attr("type","text");
        inputField.attr("placeholder","Channel Name");
        let button = $('<button/>', {});
        button.attr("id", "createChannelButton");
        button.text("Create channel");
        addNewChannel.append(inputField);
        addNewChannel.append(button);
        $("#container-fluid").prepend(addNewChannel);

        $.ajax({
            type: "GET",
            url: "/get-user-channels",
            success: response => {
                channelController.populateChannelList(response.channels)
            }
        });

    $('#createChannelButton').on("click", function(event){
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/newchannel",
            data: $('#newChannel').serialize(),
            success: response => {
                channelController.populateChannelList(response.channels);
                socketHandler.connnectToChannels();
            },
            error: response => {
            }
        });
        $('#newChannel input[name=channelName]').val("");
    });
    },

    populateChannelList : function(channels){
        $('#sidebar').empty();
            channels.forEach(function (element){
                let channelButton = $('<button/>', {}).attr("data-id",element.id).text(element.name).addClass("channelButton");

                channelButton.click(function() { channelController.loadChannelMessages(element.id) });
                $('#sidebar').append(channelButton);;
                $('#sidebar').append("<br>")

        })
    },

    loadChannelMessages : function(channelId){
       $("#main_window").html("");
       $.ajax({
           type: "GET",
           url: "/channel/" + channelId,
           success: response => {
               let channelMessagesDiv = $("<div>", {id: "channelMessagesDiv" });
               $("#main_window").append(channelMessagesDiv);
               $("#channelMessagesDiv").attr("data-channel-id", channelId);
               response.channelMessages.forEach(function (element){
                    let div = $("<div/>", {});
                    let author = $("<p/>").text(channelController.timeConverter(element.date) + "     By: "+ element.author.name);
                    let message = $("<p/>").text(element.message);
                    div.append(author).append(message);
                    $("#channelMessagesDiv").append(div);
                });
                let texterdiv = $("<div>");
                let messageInput = $("<input/>", {
                    id: "messageInput", type: "text", placeholder: "Write message here...", name: "message"
                });
                let sendMessageButton = $("<button/>", {
                    type: "submit"
                }).text("Send");

                sendMessageButton.click(function() { channelController.sendMessage(channelId) });
                messageInput.value = " ";
                $("#main_window").prepend(sendMessageButton);
                $("#main_window").prepend(messageInput);
                this.colorChannelMessages();
           }
       })
    },

    sendMessage : function(channelId){
          console.log("Send message function from channel.js")
          let message = $("#messageInput").val();
          let data = {"message": message, "channelId": channelId};
          $.ajax({
              type: "POST",
              url: "/channel/" + channelId + "/newmessage",
              data: data,
              success: response => {
                  $("#messageInput").val(' ');
                  socketHandler.sendSignalToChannel(channelId);
              }
          })
  },

    timeConverter : function(UNIX_timestamp){
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
    },

    colorChannelMessages : function(){
        $( "#channelMessagesDiv" ).children().each(function(index) {
            if(index % 2 == 0) {
                $( this ).addClass( "red" );
            } else {
                $( this ).addClass( "blue" );
            }
        });
    },

    addLastMessage : function(channelMessage){
        console.log("ADDING LAST MASSAGE FROM CHANNEL JS")
        let div = $("<div/>");
        let author = $("<p/>").text(channelController.timeConverter(channelMessage.date) + "     By: " + channelMessage.author.name);
        let message = $("<p/>").text(channelMessage.message);
        div.append(author).append(message);
        $("#channelMessagesDiv").append(div);
        channelController.colorChannelMessages();
    }

}