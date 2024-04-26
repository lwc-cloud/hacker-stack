// 154.9.253.147

function login() {
    var user = document.getElementById("user");
    var pwd = document.getElementById("pwd");

    var t = document.getElementById("wait");
    t.style.display = "block"
    try{
        var username =  user.value;
        var password = pwd.value;
        
        var xhr = new XMLHttpRequest();
        xhr.open("POST" , "http://154.201.85.154:11111/login",false);
        xhr.send(username+"\n"+password);
        
        // 返回的是 Json字符串，自己去处理,默认的信息是 {"message":"login successful."}
        var json_content = xhr.responseText;
        var json = JSON.parse(json_content);
        if (String(json.message).toLowerCase().includes("login successful.")) {
            document.cookie = "{\"user\" : \""+username+"\" , \"pwd\" : \""+password+"\"}";
            window.location.href = "/";
            return true;
        } else {
            return false; 
        }
    } catch(e) {
        alert ("登录错误: "+e);
    }
}
function reg() {
    var user = document.getElementById("user");
    var pwd = document.getElementById("pwd");
    var check_code = document.getElementById("check");
    var retype = document.getElementById("retype");

    var a = pwd.value;
    var b = retype.value;

    if (a.length < 8) {
        alert("密码不得小于 8 位字符");
        return false;
    }

    var t = document.getElementById("wait");
    t.style.display = "block"

    if (a == b) {
        try{
            var username = user.value;
            var password = pwd.value;
            var check = check_code.value;

            var xhr = new XMLHttpRequest();
            xhr.open("POST" , "http://154.201.85.154:11111/reg",true);
            xhr.send(username+"\n"+password+"\n"+check);
            
            xhr.onload = function() {
                // 返回的是 Json字符串，自己去处理,默认的信息是 {"message":"create successful."}
                var json_content = xhr.responseText;
                alert(JSON.parse(json_content).message);
                if (String(JSON.parse(json_content).message).toLowerCase().includes("create successful")) {
                    document.cookie = "{\"user\" : \""+username+"\" , \"pwd\" : \""+password+"\"}";
                    window.location.href = '/';
                }
            }
        }
        catch (e) {
            alert("注册错误: "+e);
        }
    }else{
        alert("确认密码和密码不一致")
        return false
    }
}