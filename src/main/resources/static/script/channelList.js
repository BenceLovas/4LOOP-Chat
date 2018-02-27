const channelListController = {
    buildChannelList: function(response, channelsDiv) {
        response.channels.forEach(function(channelData) {
            let div = $("<div/>", {
                "class": "row channelListItem",
            });
            let name = $("<p/>", {
                "class": "col-8 col-md-9 channelListTitle",
            }).text(channelData.channel.name + "pw :" + channelData.channel.private.toString());
            let userSize = $("<p/>", {
                "class": "col-1 channelListSize",
            }).text(channelData.channel.userList.length);
            div.append(name);
            let inputField = $('<input/>', {});
            if (channelData.channel.private && !channelData.joined) {
                name.attr("class", "col-4 col-md-6 channelListTitle");
                inputField.attr("name", "channelPassword");
                inputField.attr("type", "password");
                inputField.attr("placeholder", "Channel Password");
                inputField.attr("class", "col-4 col-md-3");
                div.append(inputField);
            }
            div.append(userSize);
            if (!channelData.joined) {
                if (!channelData.channel.private) {
                    channelListController.createSimpleChannelJoinButton(div, channelData);
                } else {
                    channelListController.createPrivateChannelJoinButton(div, channelData, inputField);
                }
            }
            channelsDiv.append(div);
        });
    },

    createSimpleChannelJoinButton: function(div, channelData) {
        let joinButton = $("<button/>");
        joinButton.attr("class", "joinChannelButton col-3 col-md-2");
        div.attr("data-id", channelData.channel.id);
        joinButton.click(function() {
            let data = {
                "channelId": $(this).parent().data("id")
            };
            $.ajax({
                type: "POST",
                url: "/add-user-to-channel",
                data: data,
                success: response => {
                    div.children()[2].remove();
                    channelController.addToChannelList(response);
                    socketHandler.connectToChannel(response.id);
                },
                error: response => {
                    console.log("error");
                }
            });
        });
        joinButton.text("Join Channel");
        div.append(joinButton);
    },

    createPrivateChannelJoinButton: function(div, channelData, inputField) {
        let joinButton = $("<button/>");
        joinButton.attr("class", "joinChannelButton col-3 col-md-2");
        div.attr("data-id", channelData.channel.id);
        joinButton.click(function() {
            console.log("joining to a private channel");
            channelController.joinPrivateChannel(channelData.channel.id, inputField.val(), div);
            inputField.val("");
        });
        joinButton.text("Join Channel");
        div.append(joinButton);
    },

    loadAllChannels: function() {
        $.ajax({
            type: "GET",
            url: "/get-all-channels",
            success: response => {
                $("#main_window").html("");
                let arr = [
                    {val: "nameASC", text: 'A to Z'},
                    {val: "nameDESC", text: 'Z to A'},
                    {val: "dateASC", text: 'Old to New'},
                    {val: "dateDESC", text: 'New to Old'}
                ];
                let sel = $("<select/>");
                sel.attr('name', 'sort');
                sel.attr('id', 'sort');
                sel.change(channelListController.loadAllChannelsBy);
                arr.forEach(function(element) {
                    sel.append($("<option/>", {
                        "class": "option"
                    }).attr('value', element.val).text(element.text));
                });
                sel.prepend($("<option/>").attr({
                    'disabled': 'disabled',
                    'selected': 'selected'
                }).text("Select an option"));

        let searchInput = $('<input/>', {type: "text", placeholder: 'Search', id: "channelSearch", "class": "col-9"});
                searchInput.keyup(channelController.channelSearcher);
                let topRow = $('<div/>', {"class": "row"});
                topRow.append(sel);
                topRow.append(searchInput);
                $("#main_window").append(topRow);
                let channelsDiv = $('<div/>', {
                    id: "channelsDiv"
                });
                this.buildChannelList(response, channelsDiv);
                $("#main_window").append(channelsDiv);
            }
        });
    },

    loadAllChannelsBy : function () {
        let selected = $('select[name=sort]').val();
        $.ajax({
            type: "GET",
            url: "/sort-by/" + selected,
            success: response => {
                let channelsDiv = $('#channelsDiv');
                channelsDiv.empty();
                channelListController.buildChannelList(response, channelsDiv);
            }

        })
    },

};

