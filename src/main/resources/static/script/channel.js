const emoticonList = {"(A)": "angel", "(K)": "kiss", "(N)": "no", "(Y)": "yes", "*": "star", "8|": "clever",
    ":#": ":zip", ":$": ":shy", ":'(": ":cry", ":(": "sad", ":)": "happy", ":@": "angry", ":D": "veryHappy",
    ":O": "surprised", ":P": "tongue", ":S": "verysad", ":|": "shocked", ";)": ":wink", "&lt;3": "heart", "^o)": "smth",
    "B)": "sunglass", "~~": "annoy"
};
const audio = new Audio('https://notificationsounds.com/sound-effects/furrow-14/download/mp3');

const channelController = {
    populateEmoticons: function(channelMessageText) {
        $.each(emoticonList, function(key, value) {
            channelMessageText.html(channelMessageText.html().split(key).join("<img src='/emoticon/" + value + "' class='emoticon'>"))
        });

        return channelMessageText;
    },

    loadChannelController: function() {
        let addNewChannel = $('<form/>', {});
        addNewChannel.attr("action", "#");addNewChannel.attr("id", "newChannel");addNewChannel.attr("class", "row");
        let inputField = $('<input/>', {});
        inputField.attr("name", "channelName");inputField.attr("type", "text");
        inputField.attr("placeholder", "Channel Name");inputField.attr("class", "col-6");
        let button = $('<button/>', {});button.attr("id", "createChannelButton");button.text("Create channel");
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

        $('#createChannelButton').on("click", function(event) {
            event.preventDefault();
            let inputfield = $('#newChannel input[name=channelName]');
            if (inputfield.val() != "") {
                $.ajax({
                    type: "POST",
                    url: "/newchannel",
                    data: $('#newChannel').serialize(),
                    success: response => {
                        //channelController.populateChannelList(response.channels);
                        channelController.addToChannelList(response);
                        socketHandler.connnectToChannels(response.id);
                    },
                    //TODO error message for taken channel name
                    error: response => {}
                });
            } // TODO error message for empty channel Name
            inputfield.val("");
        });
    },

    populateChannelList: function(channels) {
        $('#sidebar').empty();
        channels.forEach(function(element) {
            let channelButton = $('<button/>', {}).attr("data-id", element.id).text(element.name).addClass("channelButton col-12");
            channelButton.click(function() {
                channelController.loadChannelMessages(element.id)
            });
            $('#sidebar').append(channelButton);
        });
    },

    addToChannelList: function(channel) {
        let channelButton = $('<button/>', {}).attr("data-id", channel.id).text(channel.name).addClass("channelButton col-12");
        channelButton.click(function() { channelController.loadChannelMessages(channel.id)  });
        $('#sidebar').append(channelButton);
    },

    loadChannelMessages: function(channelId) {
        $("#main_window").html("");
        $.ajax({
            type: "GET",
            url: "/channel/" + channelId,
            success: response => {
                $('*[data-id=' + channelId + ']').removeClass("unreadChannelButton");
                let channelMessagesDiv = $("<div>", {
                    id: "channelMessagesDiv",
                });
                channelMessagesDiv.attr("data-channel-id", channelId);
                $("#main_window").append(channelMessagesDiv);
                response.channelMessages.forEach(function(element) {
                    let div = $("<div/>", {"class": "message",});
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
                $('#channelMessagesDiv').animate({
                    scrollTop: $('#channelMessagesDiv').prop("scrollHeight")
                }, 0);
                let messageInputDiv = $("<div/>", {
                    id: "messageInputDiv",
                    "class": "container",
                });
                let messageInputForm = $("<form/>", {
                    "class": "row",
                });
                let messageInput = $("<div/>", {
                    id: "messageInput",
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
                sendMessageButton.click(function() {
                    channelController.sendMessage(channelId)
                });
                messageInput.keyup(function(e) {
                    channelController.inputChecker(e, channelId)
                });
                messageInputForm.append(messageInput);
                messageInputForm.append(sendMessageButton);
                messageInputDiv.append(messageInputForm);
                $("#main_window").append(messageInputDiv);
            }
        })
    },

    sendMessage: function(channelId) {
        event.preventDefault();
        let inputField = $("#messageInput");
        //Converting back emoticons into keys
        let emoticons = document.getElementsByClassName("emoticon");
        for (i = emoticons.length - 1; i > -1; i--) {
            if (document.getElementById("messageInput").contains(emoticons[i])) {
                let emoticonValue = emoticons[i].getAttribute("src").split("/").pop();
                for (const [key, value] of Object.entries(emoticonList)) {
                    if (value === emoticonValue) {
                        emoticons[i].parentNode.replaceChild(document.createTextNode(key), emoticons[i]);
                    }
                }
            }
        }
        let message = inputField.text();
        let data = {
            "message": message,
            "channelId": channelId
        };
        if (message != "") {
            $.ajax({
                type: "POST",
                url: "/channel/" + channelId + "/newmessage",
                data: data,
                success:  function() {
                    inputField.html("");
                    socketHandler.sendSignalToChannel(channelId);
                },
            });
        } // TODO error message to for empty message
    },

    timeConverter: function(UNIX_timestamp) {
        const a = new Date(UNIX_timestamp);
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        const year = a.getFullYear();
        const month = months[a.getMonth()];
        const date = a.getDate();
        const hour = a.getHours();
        const min = a.getMinutes();
        if (min.toString().length < 2) {
            return date + ' ' + month + ' ' + year + ' ' + hour + ':0' + min;
        } else {
            return date + ' ' + month + ' ' + year + ' ' + hour + ':' + min;
        }
    },

    addLastMessage: function(channelMessage) {
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
        $('#channelMessagesDiv').animate({
            scrollTop: $('#channelMessagesDiv').prop("scrollHeight")
        }, 500);
    },

    signalUnreadChannel: function(channelId) {
        $('*[data-id=' + channelId + ']').addClass("unreadChannelButton");
        audio.play();
    },

    inputChecker: function(event, channelId) {
        if (event.keyCode == 13) {
            channelController.sendMessage(channelId);
        }

        channelMessageText = $('#messageInput');
        let oldText = channelMessageText.html();
        let newText = oldText;
        $.each(emoticonList, function(key, value) {
            newText = newText.replace(key, "<img src='/emoticon/" + value + "' class='emoticon'>");
        });

        if (newText !== oldText) {
            channelMessageText.html(newText);
            channelController.placeCaretAtEnd(document.getElementById("messageInput"));
        }
    },

    placeCaretAtEnd: function(el) {
        el.focus();
        if (typeof window.getSelection != "undefined" &&
            typeof document.createRange != "undefined") {
            const range = document.createRange();
            range.selectNodeContents(el);
            range.collapse(false);
            const sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        } else if (typeof document.body.createTextRange != "undefined") {
            const textRange = document.body.createTextRange();
            textRange.moveToElementText(el);
            textRange.collapse(false);
            textRange.select();
        }
    },

    createPrivateChannel: function(name, password){
        $.ajax({
            type: "POST",
            url: "/new-private-channel",
            data: {channelName: "channelName", password : "password"},
            success: response => {
                channelController.addToChannelList(response);
                socketHandler.connnectToChannels(response.id);
            },
            //TODO error message for taken channel name
            error: response => {}
        });
    },

    joinPrivateChannel : function(channelId, password){
        $.ajax({
            type: "POST",
            url: "/add-user-to-private-channel",
            data: {channelId : 1, password : "password"},
            success: response => {
                channelController.addToChannelList(response);
                socketHandler.connnectToChannels(response.id);
            },
            //TODO error message for taken channel name
            error: response => {}
        });
    }

};