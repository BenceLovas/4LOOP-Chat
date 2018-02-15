var emoticonList = {
    "(A)":"(A)", "(K)":"(K)", "(N)":"(N)", "(Y)":"(Y)",
    "*":"*", "8|":"8|", ":#":":zip", ":$":":shy",
    ":'(":":cry", ":(":":(", ":)":":)", ":@":":@", ":D":":D", ":O":":O",
    ":P":":P", ":S":":S",":|":":|", ";)":":wink", "&lt;3":"<3", "^o)":"^o)", "B)":"B)", "~~":"~~"};
var audio = new Audio('https://notificationsounds.com/sound-effects/furrow-14/download/mp3');
var channelController = {
    populateEmoticons : function(channelMessageText){
        $.each(emoticonList, function(key, value){
            channelMessageText.html(channelMessageText.html().split(key).join("<img src='/emoticon/" + value + "' class='emoticon'>"))
        });

        return channelMessageText;
    },
    loadChannelController : function (){
        let addNewChannel = $('<form/>', {});
        addNewChannel.attr("action", "#");
        addNewChannel.attr("id", "newChannel");
        addNewChannel.attr("class", "row");
        let inputField = $('<input/>', {});
        inputField.attr("name","channelName");
        inputField.attr("type","text");
        inputField.attr("placeholder","Channel Name");
        inputField.attr("class","col-6");
        let button = $('<button/>', {});
        button.attr("id", "createChannelButton");
        button.text("Create channel");
        button.attr("class", "col-6");
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
            let channelButton = $('<button/>', {}).attr("data-id",element.id).text(element.name).addClass("channelButton col-12");
            channelButton.click(function() { channelController.loadChannelMessages(element.id) });
            $('#sidebar').append(channelButton);
        });
    },

    loadChannelMessages : function(channelId){
       $("#main_window").html("");
       $.ajax({
           type: "GET",
           url: "/channel/" + channelId,
           success: response => {
                $('*[data-id='+channelId + ']').removeClass("unreadChannelButton");
                let channelMessagesDiv = $("<div>", {
                    id: "channelMessagesDiv",
                });
                channelMessagesDiv.attr("data-channel-id", channelId);
                $("#main_window").append(channelMessagesDiv);
                response.channelMessages.forEach(function (element){
                    let div = $("<div/>", {
                        "class": "message",
                    });
                    let date = $("<p/>", {
                        "class": "messageDate",
                    }).text(channelController.timeConverter(element.date));
                    let author = $("<p/>", {
                        "class": "messageAuthor",
                    }).text(element.author.name);
                    let message = channelController.populateEmoticons($("<p/>", {
                        "class": "messageText",
                    }).text(element.message));
                    div.append(author).append(message).append(date);
                    $("#channelMessagesDiv").append(div);
                });
                $('#channelMessagesDiv').animate({scrollTop: $('#channelMessagesDiv').prop("scrollHeight")}, 0);
                let messageInputDiv = $("<div/>", {
                    id: "messageInputDiv",
                    "class": "container",
                });
                let messageInputForm = $("<form/>", {
                    "class": "row",
                });
                let messageInput = $("<div/>", {
                    id: "messageInput",
                    type: "submit",
                    "contentEditable": "true",
                    "class": "col-9 message",
                    placeholder: "Write message here...",
                    name: "message",
                });
                let sendMessageButton = $("<button/>", {
                    id: "sendMessage",
                    "class": "col-3",
                    type: "submit",
                }).text("Send");
                sendMessageButton.click(function() { channelController.sendMessage(channelId) });
                messageInputForm.append(messageInput);
                messageInputForm.append(sendMessageButton);
                messageInputDiv.append(messageInputForm);
                $("#main_window").append(messageInputDiv);
           }
       })
    },
    sendMessage : function(channelId){
      event.preventDefault();
      let inputField = $("#messageInput");
      let emoticons = document.getElementsByClassName("emoticon");
      for(i = emoticons.length-1; i>-1; i--){
        if(document.getElementById("messageInput").contains(emoticons[i])){
            let emoticonValue = emoticons[i].getAttribute("src").split("/").pop();
            for(const [key, value] of Object.entries(emoticonList)){
                if(value === emoticonValue){
                    emoticons[i].parentNode.replaceChild(document.createTextNode(key), emoticons[i]);
                }
            }
        }
      }

      let message = inputField.text();
      let data = {"message": message, "channelId": channelId};
      $.ajax({
          type: "POST",
          url: "/channel/" + channelId + "/newmessage",
          data: data,
          success: response => {
              inputField.html("");
              socketHandler.sendSignalToChannel(channelId);
          }
      });
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

    addLastMessage : function(channelMessage){
        let div = $("<div/>", {
            "class": "message",
        });
        let date = $("<p/>", {
            "class": "messageDate",
        }).text(channelController.timeConverter(channelMessage.date));
        let author = $("<p/>", {
            "class": "messageAuthor",
        }).text(channelMessage.author.name);
        let message = channelController.populateEmoticons($("<p/>", {
            "class": "messageText",
        }).text(channelMessage.message));
        div.append(author).append(message).append(date);
        $("#channelMessagesDiv").append(div);
        $('#channelMessagesDiv').animate({scrollTop: $('#channelMessagesDiv').prop("scrollHeight")}, 500);
    },

    signalUnreadChannel : function(channelId){
        $('*[data-id='+channelId + ']').addClass("unreadChannelButton");
        audio.play();
    },

};
