var channelListController = {
    loadAllChannels : function(){
        $.ajax({
            type: "GET",
            url: "/get-all-channels",
            success: response => {
                $("#main_window").html("");
                response.channels.forEach(function (channelData){
                    let div = $("<div/>", {
                        "class": "row",
                    });
                    let name = $("<p/>", {
                        "class": "col-8",
                    }).text(channelData.channel.name);
                    let userSize = $("<p/>", {
                        "class": "col-2",
                    }).text(channelData.channel.userList.length);
                    div.append(name);
                    div.append(userSize);
                    if (!channelData.joined){
                        let joinButton = $("<button/>");
                        joinButton.attr("class", "joinChannelButton col-2");
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
                    $("#main_window").append(div);
                });
            }
        });
    },
};