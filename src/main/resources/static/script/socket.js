var socketHandler = {

    //-----------WORK IN PROGRESS-------------------------------

    /*
        this containts all the socket connection that the user has
        1 socket can only subscribe to 1 channel
        Its a global variable so we should do something about it
        plus we need a better naming pattern for the keys
        current is channel<ID>

    */
    stompClients : {},

    //Need to replace the for cycle for all the channel that the user has
    connnectToChannels : function() {
        var channelIdList = [];

        $.ajax({
            type: "GET",
            url: "/get-all-user-channel-id",
            success: function(data) {
                $.each(data.channelIds, function (index, value) {
                    console.log("Iteration vlaue is :" + value);
                    let socket = new SockJS('/gs-guide-websocket');
                    //"channel + channel.id in the future
                    socketHandler.stompClients["channel" + value] = Stomp.over(socket);
                    socketHandler.stompClients["channel" + value].connect({}, function (frame) {
                        console.log('Connected: ' + frame);
                        socketHandler.stompClients["channel" + value].subscribe('/socket-listener/channel/' + value, function (channelMessage) {
                            socketHandler.reactSignal(channelMessage);
                        });
                    });
                });
                }
            });
    },

    /*
    Everytime someone writes a message, this method must be called
    We can also pass any object to it, and all the subcrived user can get that object
     */

    sendSignalToChannel : function (channelId) {
        socketHandler.stompClients["channel" + channelId].send("/socket-storer/channel/" + channelId, {}, JSON.stringify({"message" : "anyad"}));
    },


    reactSignal : function (jsonmsg) {
        let channelMessage = JSON.parse(jsonmsg.body).body.channelMessage;
        if(channelMessage.channelId == $("#channelMessagesDiv").attr("data-channel-id")){
            let div = $("<div/>");
            let author = $("<p/>").text(channelController.timeConverter(channelMessage.date) +  "     By: "+ channelMessage.author.name);
            let message = $("<p/>").text(channelMessage.message);
            div.append(author).append(message);
            $("#channelMessagesDiv").append(div);
        }
        console.log("YOU HAVE A NEW MESSAGE AT SOME OTHER CHANNEL, specificly at =" + channelMessage.channelId);
    }
};

