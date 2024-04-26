function load_index_text() {

    auto_login()

    var t = document.getElementById('t');
    var text_1 = `
Hacker Stack系统并不是一个界面效果或者是一个用来装逼的工具。而是一个真实
n
允许初学者利用n
各种可视化或者是半可视化工具便可n
n
以完成渗透测试、社会工程学渗透、木马渗透、Web渗透等的综合性
n
在线渗透测试工具，主要是面向初学者快速的对网安进行学习和渗透测试操作，你在短时间内成为黑客
 n不是梦想
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
        if (splitText_1[i] == "n") {
            t.innerHTML+= "<br />";
        }else {
            t.innerHTML+= "<a style='color:red'>"+splitText_1[i]+"</a>";
        }
    } , 100);
}

window.onload = function() {
    load_index_text()
    connect_to_db()
}

function connect_to_db () {
    var db_c = document.getElementById('connect');
    if (true) {
        var text = `
 连接 Linwin MetaLite Server 数据库服务系统，将会存储你所有的用户测试数据以及你需要存储的内容.
 作为渗透测试数据库,请做好反渗透与渗透.
    `;
    var t_1 = document.getElementById('db_content');
    var splitText_2 = text.split('');

    var run = null;
    var i = 0;
    run = setInterval(function() {
        i++
        if (i + 1 == text.length) {
            clearInterval(run);
            return;
        }
        if (splitText_2[i] == "\n") {
            t_1.innerHTML+= "<br />";
        }else {
            t_1.innerHTML+= "<a style='color:white'>"+splitText_2[i]+"</a>";
        }
    } , 100);
    } else {
        db_c.innerText = "未连接数据库";
    }
}

function login_ok() {
    var get_result = false;
    try {
        var json = JSON.parse(document.cookie);
        if (json.user != null && json.pwd != null)
        {
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
        }else{
            document.cookie = "{\"user\":\"\",\"pwd\":\"\"}";
        }
        return get_result;
    }catch (e) {
        return get_result;
    }
}


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
        document.cookie = '';
        document.cookie = "{\"user\" : \"\" , \"pwd\" : \"\"}";
        return false;
    }
}