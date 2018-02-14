var channelListController = {
    loadAllChannels : function(){
        $.ajax({
            type: "GET",
            url: "/get-all-channels",
            success: response => {
                $("#main_window").html("");
                response.channels.forEach(function (channelData){
                    let div = $("<div/>", {});
                    let name = $("<p/>").text(channelData.channel.name);
                    name.attr("class", "channelList");
                    let userSize = $("<p/>").text("size of the channel : " + channelData.channel.userList.length);
                    userSize.attr("class", "channelList");
                    div.append(name);
                    div.append(userSize);
                    if (!channelData.joined){
                        let joinButton = $("<button/>");
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
                        joinButton.text("Join channel");
                        div.append(joinButton);

                    }
                    $("#main_window").append(div);
                });
            }
        });
    },
};