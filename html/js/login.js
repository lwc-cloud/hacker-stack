// 154.9.253.147
//var remote = 'http://127.0.0.1:11111/'
var remote = 'http://stack.tiaha.cn:11111'

function login() {
    var user = document.getElementById("user");
    var pwd = document.getElementById("pwd");

    var t = document.getElementById("wait");
    t.style.display = "block"
    try{
        var username =  user.value;
        var password = pwd.value;
        
        var xhr = new XMLHttpRequest();
        xhr.open("POST" , remote+"/login",false);
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
        showAlert ("登录错误: "+e , null);
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
        showAlert("密码不得小于 8 位字符" , null);
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
            xhr.open("POST" , remote+"/reg",true);
            xhr.send(username+"\n"+password+"\n"+check);
            
            xhr.onload = function() {
                // 返回的是 Json字符串，自己去处理,默认的信息是 {"message":"create successful."}
                var json_content = xhr.responseText;
                showAlert(JSON.parse(json_content).message , null);
                if (String(JSON.parse(json_content).message).toLowerCase().includes("create successful")) {
                    document.cookie = "{\"user\" : \""+username+"\" , \"pwd\" : \""+password+"\"}";
                    window.location.href = '/';
                }
            }
        }
        catch (e) {
            showAlert("注册错误: "+e , null);
        }
    }else{
        showAlert("确认密码和密码不一致" , null)
        return false
    }
}