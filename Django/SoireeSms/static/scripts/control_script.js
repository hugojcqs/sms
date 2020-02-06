var socket;
window.onload = function() {
    var wsStart = 'ws://localhost:8000/feed';
    socket = new WebSocket(wsStart);

    socket.onmessage = function (e) {
        data = JSON.parse(e['data']);
        if(data['action'] === 'allow')
        {
            btn = $("#auth"+data['id']);
            btn.removeClass('btn-danger');
            btn.addClass("btn-success disabled");
            btn.text("Authorized");
            console.log('ok')
        }
        else if(data['action'] === 'new_sms')
        {
            sms = data['sms'];
            sms_id = data['id'];
            number = data['number'];
            time = data['date_time'];

                raw_html = `<a class="list-group-item list-group-item-action flex-column align-items-start text-white history_color" style="background-color:#8b9dc3;"><div class="d-flex w-100 justify-content-between"><h5 class="mb-1">${sms}</h5><div class="btn btn-sm btn-danger history_btn" id="auth${sms_id}" onclick="authorize_sms(${sms_id})">Authorize</div></div><p class="mb-1">${number}</p><small>${time}</small></a>`;
                $('#histo').prepend(raw_html)
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

function authorize_sms(id){
    allow_dict = {'action':'allow','id':id};
    socket.send(JSON.stringify(allow_dict))
}