

/*
    this containts all the socket connection that the user has
    1 socket can only subscribe to 1 channel
    Its a global variable so we should do something about it
    plus we need a better naming pattern for the keys
    current is channel<ID>
*/
var stompClients = {};

//-----------WORK IN PROGRESS-------------------------------

function connnectToChannels() {
    //We should iterate trough all the channels that the user in
    for(let i = 0; i < 5; i++){
        let socket = new SockJS('/gs-guide-websocket');
        //"channel + channel.id in the future
        stompClients["channel" + i] = Stomp.over(socket);
        stompClients["channel" + i].connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClients["channel" + i].subscribe('/socket-listener/channel/' + i, function (message) {
                reactSignal(i);
            });
        });
    }
}

//Everytime someone writes a message, this method must be called
//We can also pass any object to it, and all the subcrived user can get that object
function sendSignalToChannel(channelId) {
    stompClients["channel" + channelId].send("/socket-storer/channel/" + channelId, {}, JSON.stringify({"message" : "anyad"}));
}

function reactSignal(channelId) {
    console.log("RECEIVED A NEW MESSAGE TO CHANNEL:" + channelId);
}