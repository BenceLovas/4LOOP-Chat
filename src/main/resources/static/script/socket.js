
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
        for(let i = 0; i < 5; i++){
            let socket = new SockJS('/gs-guide-websocket');
            //"channel + channel.id in the future
            socketHandler.stompClients["channel" + i] = Stomp.over(socket);
            socketHandler.stompClients["channel" + i].connect({}, function (frame) {
                console.log('Connected: ' + frame);
                socketHandler.stompClients["channel" + i].subscribe('/socket-listener/channel/' + i, function (message) {
                    socketHandler.reactSignal(i);
                });
            });
        }
    },

    /*
    Everytime someone writes a message, this method must be called
    We can also pass any object to it, and all the subcrived user can get that object
     */

    sendSignalToChannel : function (channelId) {
        socketHandler.stompClients["channel" + channelId].send("/socket-storer/channel/" + channelId, {}, JSON.stringify({"message" : "anyad"}));
    },

    reactSignal : function (channelId) {
        console.log("RECEIVED A NEW MESSAGE TO CHANNEL:" + channelId);
    }
};