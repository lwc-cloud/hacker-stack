
var remote = 'https://user.hackerstack.top'

function load_index_text() {

    auto_login()

    var t = document.getElementById('t');
    var text_1 = `
Hacker Stack系统并不是一个界面效果或者是一个用来装逼的工具。而是一个真实允许初学者利用各种可视化或者是半可视化工具便可以
完成渗透测试的在线网站
    `
    var splitText_1 = text_1.split('');

    var run = null;
    var i = 0;
    run = setInterval(function() {
        i++
        if (i + 1 == text_1.length) {
            clearInterval(run);
            return;
        }
        if (splitText_1[i] == "\n") {
            t.innerHTML+= "<br />";
        }else {
            t.innerHTML+= "<a style='color:red'>"+splitText_1[i]+"</a>";
        }
    } , 100);
}

function default_login() {
    document.cookie = '{"user" : "linux" , "pwd" : "linux"}'
    showAlert("成功以访客身份登录最好注册一个，因为有一些服务是互通的，访客账户账户密码都是开放的，无法保证任何的服务安全" , null);
    setTimeout(function() {
        window.location.href = ''
    }, 5000);
}

window.onload = function() {
    load_index_text()
}

function login_ok(to_system , showAlert) {
    var get_result = false;
    try {
        var json = JSON.parse(document.cookie);
        if (json.user != null && json.pwd != null)
        {
            var json = JSON.parse(document.cookie);
            var username =  json.user;
            var password = json.pwd;
            
            var xhr = new XMLHttpRequest();
            xhr.open("POST" , remote+"/login",true);
            xhr.send(username+"\n"+password);
            
            xhr.onload=function() {
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
                    to_system()
                    return true;
                } else {
                    showAlert("请先登录" , null)
                    return false; 
                }
            }
        }else{
            showAlert("请先登录" , null)
            document.cookie = "{\"user\":\"\",\"pwd\":\"\"}";
        }
        return get_result;
    }catch (e) {
        showAlert("请先登录" , null)
        return get_result;
    }
}


function auto_login() {

    try{
        var json = JSON.parse(document.cookie);
        var username =  json.user;
        var password = json.pwd;
        
        var xhr = new XMLHttpRequest();
        xhr.open("POST" , remote+"/login",false);
        xhr.send(username+"\n"+password);
        console.log(18293012038098)
        
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
            var top = document.getElementById('top');
            var exit_login = document.createElement('a');
            exit_login.onclick=function() {
                document.cookie = '';
                window.location.href=''
            }
            exit_login.innerText = '退出登录'
            top.appendChild(exit_login);
            document.getElementById('default_login').style.display = 'none'
            return true;
        } else {
            return false; 
        }
    } catch(e) {
        document.cookie = '';
        document.cookie = "{\"user\" : \"\" , \"pwd\" : \"\"}";
        return false;
    }
}