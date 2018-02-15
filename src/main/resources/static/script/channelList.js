var channelListController = {
    loadAllChannels : function(){
        $.ajax({
            type: "GET",
            url: "/get-all-channels",
            success: response => {
                socketHandler.connnectToChannels();
                $("#main_window").html("");
                let arr = [
                    {val: "nameASC", text: 'Name ascending'},
                    {val: "nameDESC", text: 'Name descending'},
                    {val: "dateASC", text: 'Newest first'},
                    {val: "dateDESC", text: 'Oldest first'}
                ];
                let sel = $("<select/>");
                sel.attr('name', 'sort');
                sel.attr('id', 'sort');
                sel.change(channelListController.loadAllChannelsBy);
                arr.forEach(function(element){
                    sel.append($("<option/>").attr('value', element.val).text(element.text));
                });
                sel.prepend($("<option/>").attr({'disabled' : 'disabled', 'selected' : 'selected'}).text("Select an option"));
                $("#main_window").append(sel);
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
                                    console.log("im here")
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
    loadAllChannelsBy : function () {
        let selected = $('select[name=sort]').val();
        console.log(selected);
        $.ajax({
            type: "GET",
            url: "/sort-by/" + selected,
            success: response => {
                $("#main_window").html("");
                let arr = [
                    {val: "nameASC", text: 'Name ascending'},
                    {val: "nameDESC", text: 'Name descending'},
                    {val: "dateASC", text: 'Newest first'},
                    {val: "dateDESC", text: 'Oldest first'}
                ];
                let sel = $("<select/>");
                sel.attr('name', 'sort');
                sel.attr('id', 'sort');
                sel.change(channelListController.loadAllChannelsBy);
                arr.forEach(function(element){
                    sel.append($("<option/>").attr('value', element.val).text(element.text));
                });
                $("#main_window").append(sel);
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

        })
    },

};

