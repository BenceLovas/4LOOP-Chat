const channelListController = {
    loadAllChannels : function(){
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
                let sel = $("<select/>", {"class": "col-3"});
                sel.attr('name', 'sort');
                sel.attr('id', 'sort');
                sel.change(channelListController.loadAllChannelsBy);
                arr.forEach(function(element){
                    sel.append($("<option/>", {"class": "option"}).attr('value', element.val).text(element.text));
                });
                sel.prepend($("<option/>").attr({'disabled' : 'disabled', 'selected' : 'selected'}).text("Select an option"));


                let searchInput = $('<input/>', {type: "text", placeholder: 'Search', id: "channelSearch", "class": "col-9"});
                searchInput.keyup(channelController.channelSearcher);
                let topRow = $('<div/>', {"class": "row"});
                topRow.append(sel);
                topRow.append(searchInput);
                $("#main_window").append(topRow);

                let channelsDiv = $('<div/>', {id: "channelsDiv"});
                response.channels.forEach(function (channelData){
                    let div = $("<div/>", {
                        "class": "row channelListItem",
                    });
                    let name = $("<p/>", {
                        "class": "col-6 col-md-8 channelListTitle",
                    }).text(channelData.channel.name);
                    let userSize = $("<p/>", {
                        "class": "col-2 channelListSize",
                    }).text(channelData.channel.userList.length);
                    div.append(name);
                    div.append(userSize);
                    if (!channelData.joined){
                        let joinButton = $("<button/>");
                        joinButton.attr("class", "joinChannelButton col-4 col-md-2");
                        div.attr("data-id", channelData.channel.id);
                        joinButton.click(function(){
                            let data = {"channelId": $(this).parent().data("id")};
                            $.ajax({
                                type: "POST",
                                url: "/add-user-to-channel",
                                data: data,
                                success: response => {
                                    channelController.populateChannelList(response);
                                    channelListController.loadAllChannels();
                                    socketHandler.connectToChannel($(this).parent().data("id"));
                                },
                                error: response => {
                                    console.log("error");
                                }
                            });
                        });
                        joinButton.text("Join Channel");
                        div.append(joinButton);
                    }
                    channelsDiv.append(div);
                });
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
                response.channels.forEach(function (channelData){
                    let div = $("<div/>", {
                        "class": "row channelListItem",
                    });
                    let name = $("<p/>", {
                        "class": "col-6 col-md-8 channelListTitle",
                    }).text(channelData.channel.name);
                    let userSize = $("<p/>", {
                        "class": "col-2 channelListSize",
                    }).text(channelData.channel.userList.length);
                    div.append(name);
                    div.append(userSize);
                    if (!channelData.joined){
                        let joinButton = $("<button/>");
                        joinButton.attr("class", "joinChannelButton col-4 col-md-2");
                        div.attr("data-id", channelData.channel.id);
                        joinButton.click(function(){
                            let data = {"channelId": $(this).parent().data("id")};
                            $.ajax({
                                type: "POST",
                                url: "/add-user-to-channel",
                                data: data,
                                success: response => {
                                    channelController.populateChannelList(response);
                                    channelListController.loadAllChannels();
                                },
                                error: response => {
                                    console.log("error");
                                }
                            });
                        });
                        joinButton.text("Join Channel");
                        div.append(joinButton);
                    }
                    channelsDiv.append(div);
                });
            }

        })
    },

};

