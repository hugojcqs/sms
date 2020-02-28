var socket;
window.onload = function() {

    let hostname = window.location.hostname;

    var wsStart = 'ws://' + hostname + '/display';
    socket = new WebSocket(wsStart);

    socket.onmessage = function (e) {
        data = JSON.parse(e['data']);
        if(data['action'] === 'display')
        {
            console.log(data)
            message = data['message']
            number = data['number']
            console.log(message)
            console.log(number)
        }
        else if(data['action'] === 'display_image')
        {
            image_b64 = data['image']
             $('#container').html('<img src="data:image/jpg;base64,' + image_b64 + '" />');

        }
    };

    socket.onopen = function (e) {
        console.log("open", e)
    };

    socket.onerror = function (e) {
        console.log("error", e)
    };

    socket.onclose = function (e) {
        console.log("close", e)
    };
};