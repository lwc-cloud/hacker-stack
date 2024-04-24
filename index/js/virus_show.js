function auto_login() {

    try{
        var json = JSON.parse(document.cookie);
        var username =  json.user;
        var password = json.pwd;
        
        var xhr = new XMLHttpRequest();
        xhr.open("POST" , "http://154.201.85.154:11111/login",false);
        xhr.send(username+"\n"+password);
        
        // 返回的是 Json字符串，自己去处理,默认的信息是 {"message":"login successful."}
        var json_content = xhr.responseText;
        var json = JSON.parse(json_content);
        if (String(json.message).toLowerCase().includes("login successful.")) {
            document.cookie = "{\"user\" : \""+username+"\" , \"pwd\" : \""+password+"\"}";
            
            var login_btn = document.getElementById('login');
            login_btn.innerText = "登录: "+username;
            login_btn.onclick = function() {
                window.location.href = "./user.html";
            }
            return true;
        } else {
            return false; 
        }
    } catch(e) {
        return false;
    }
}

window.onload = function() {
    auto_login();
    
}