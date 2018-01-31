$(function() {

    function getUsers() {
        $.ajax({
            type: "GET",
            url: "/users",
            success: data => {
                let div = $('<div/>', {});
                data.forEach((user) => {
                    div.append($('<p/>').text(user.id + " " + user.name))
                });
                $('#usersWrapper').append(div);
            }
        })
    }



    $('#getUsersButton').click(getUsers);

});
	function getMessages() {
		$.ajax({
			url: "/get-messages",
			dataType: "json",
			type: "GET",
			success: function(data) {
                var targetTable = document.getElementById("chattable")
                targetTable.innerHTML = ''
                for(var i = 0;i < data.length;i++){
                    var newtd = document.createElement("td")
                    var newtr = document.createElement("tr")
                    newtr.appendChild(newtd)
                    newtd.innerHTML += data[i].message
                    targetTable.appendChild(newtr)
                }
                var tdList = document.getElementsByTagName("td")
                for(var j = 0;j < tdList.length;j++){
                    if(j % 2 == 0){
                    tdList[j].className ="red";
                    }else{
                        tdList[j].className = "blue";
                    }
			    }
	        }
        })
    }

    function sendAjax() {
        $.ajax({
            url: '/write_message',                //function route to give the data to
            type: 'POST',                       //methods =['POST'] must be added to the function in the server.py
            data: {                             //data must be an object a json format object
                'message': $( '#new_message' ).val(),    //getting input field value
            },
            success: function(){                //on success function
                $( '#new_message' ).val('')
                getMessages()
            }
        })
    }

function sayhello(){
console.log("hello)")
}

