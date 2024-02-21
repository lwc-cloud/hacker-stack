
var remote = "http://127.0.0.1:5555";

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine";
}

function social_attack(attack_type) {
    var user = JSON.parse(document.cookie).user;
    var pwd = JSON.parse(document.cookie).pwd;
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/"+attack_type+"/"+user+"/"+pwd,true);
    xhr.send();
    xhr.onload=function()
    {
        if (xhr.readyState === 4) {
            var response = xhr.responseText;
            showAlert('Please Visit URL: ' + remote + '/'+response , null)

            var run = setInterval(function() 
            {
                var get_message = new XMLHttpRequest();
                get_message.open(
                    'GET',remote+"/run/"+response , true
                    );
                get_message.send();
                get_message.onload = function() 
                {
                    if (get_message.readyState === 4) 
                    {
                        var r = get_message.responseText;
                        if (r.replace('\n','') != 'none')
                        {
                            showAlert("<h2 style='color:red'>收到信息:</h2>"+r, null)
                        }
                    }
                }
            } , 400); //使用短轮询，不是 websocket 用不起，而是短轮询更有性价比.
        }
    }
}
function web_clone() {
    var url = prompt('输入需要克隆的地址: (不要加 http://或https://)')
    if (url == null) {
        return;
    }
    else {
        var user = JSON.parse(document.cookie).user;
        var pwd = JSON.parse(document.cookie).pwd;
        var xhr = new XMLHttpRequest();
        xhr.open('GET',remote+"/WebClone/"+url+"/"+user+"/"+pwd , true);
        xhr.send();
        xhr.onload=function() {
            if (xhr.readyState == 4) {
                var response = xhr.responseText;
                console.log(response)
                showAlert('Please Visit URL: <a herf="_black">'+remote+'/w/'+response+'</a>' , null)
            }
        }
    }
}