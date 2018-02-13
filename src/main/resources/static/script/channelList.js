var channelListController = {
    loadAllChannels : function(){
        $.ajax({
            type: "GET",
            url: "/get-all-channels",
            success: response => {
            $("#main_window").html("");
            response.channels.forEach(function (channel){
                let div = $("<div/>", {});
                let name = $("<p/>").text(channel.name);
                name.attr("class", "channelList");
                let userSize = $("<p/>").text("size of the channel : " + channel.userList.length);
                userSize.attr("class", "channelList");
                let joinButton = $("<button/>");
                div.attr("data-id", channel.id);
                joinButton.click(function(){
                    let data = {"channelId": $(this).parent().data("id")};
                    console.log(data);
                    $.ajax({
                        type: "POST",
                        url: "/add-user-to-channel",
                        data: data,
                        success:channelController.populateChannelList,
                        error: console.log("error")
                    });
                });
                joinButton.text("Join channel");
                div.append(name);
                div.append(userSize);
                div.append(joinButton);
                $("#main_window").append(div);
                });
                }
            });
        },
}