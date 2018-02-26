const socketHandler = {

    //-----------WORK IN PROGRESS-------------------------------

    /*
        this containts all the socket connection that the user has
        1 socket can only subscribe to 1 channel
        Its a global variable so we should do something about it
        plus we need a better naming pattern for the keys
        current is channel<ID>

    */
    stompClients: {},

    //Need to replace the for cycle for all the channel that the user has
    connnectToChannels: function () {
        const channelIdList = [];

        $.ajax({
            type: "GET",
            url: "/get-all-user-channel-id",
            success: function (data) {
                $.each(data.channelIds, function (index, value) {
                    if(!("channel" + value in socketHandler.stompClients)){
                        let socket = new SockJS('/gs-guide-websocket');
                        socketHandler.stompClients["channel" + value] = Stomp.over(socket);
                        socketHandler.stompClients["channel" + value].connect({}, function (frame) {
                            socketHandler.stompClients["channel" + value].subscribe('/socket-listener/channel/' + value, function (channelMessage) {
                                socketHandler.reactSignal(channelMessage);
                            });
                        });
                    }
                });
            }
        });
    },

    connectToChannel: function(channelId){
        if(!("channel" + channelId in socketHandler.stompClients)){
            let socket = new SockJS('/gs-guide-websocket');
            socketHandler.stompClients["channel" + channelId] = Stomp.over(socket);
            socketHandler.stompClients["channel" + channelId].connect({}, function (frame) {
                socketHandler.stompClients["channel" + channelId].subscribe('/socket-listener/channel/' + channelId, function (channelMessage) {
                    socketHandler.reactSignal(channelMessage);
                });
            });
        }
    },

    /*
    Everytime someone writes a message, this method must be called
    We can also pass any object to it, and all the subcrived user can get that object
     */

    sendSignalToChannel: function (channelId) {
        socketHandler.stompClients["channel" + channelId].send("/socket-storer/channel/" + channelId, {}, JSON.stringify({"message": "anyad"}));
    },

    reactSignal: function (jsonmsg) {
        let channelMessage = JSON.parse(jsonmsg.body).body.channelMessage;
        let channelId = JSON.parse(jsonmsg.body).body.channelId;
        if (channelId == $("#channelMessagesDiv").attr("data-channel-id")) {
            channelController.addLastMessage(channelMessage);
        } else {
            channelController.signalUnreadChannel(channelId);
        }
    }
};

