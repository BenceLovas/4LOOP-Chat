function addGiphy(query){
    let query = 'rainbow';
    
    request = new XMLHttpRequest;
    request.open('GET', 'http://api.giphy.com/v1/gifs/search?q=' + query + 'rainbow&limit=1&api_key=LvFaU080M1IOx6M65najRrclnff8moJY&', true);

    request.onload = function(){
        if (request.status >= 200 && request.status < 400) {
            data = JSON.parse(request.responseText);
            console.log(data);
            $('#giphy').html('<img src="'+data+'" title="GIF via GIPHY" align="middle">');

        } else {
            console.log('API error');
        }
    };

     request.onerror = function(){
         console.log('Connection error');
     }
     request.send();

}