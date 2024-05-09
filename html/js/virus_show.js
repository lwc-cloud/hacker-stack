function auto_login() {

    try{
        var json = JSON.parse(document.cookie);
        var username =  json.user;
        var password = json.pwd;
        
        var xhr = new XMLHttpRequest();
        xhr.open("POST" , "http://stack.tiaha.cn:11111/login",false);
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

function load_virus_list() {
    console.log(1)
    var xhr = new XMLHttpRequest();
    xhr.open("GET" , remote+"/get_virus_list" , true);
    xhr.send();
    var virus_show = document.getElementById('virus_show');
    xhr.onload = function() {
        var content = xhr.responseText;
        var content_split = content.split('\n');
        for (var i = 0 ; i < content_split.length ; i++) {
            (function(file_name) {
                if (file_name != '') {
                    var btn = document.createElement('button');
                    btn.innerText = file_name;
                    btn.id = file_name;
                    btn.className = 'btn_1';
                    btn.style.width='100%';
                    virus_show.appendChild(btn);

                    btn.onclick = function()
                    {
                        var a = document.createElement('a');
                        a.download = remote+"/get_virus/"+btn.id;
                        a.href = remote+"/get_virus/"+btn.id
                        a.click();
                    }
                }
            }) (content_split[i])
        }
    }
}

window.onload = function() {
    document.getElementById('update_file').addEventListener('change', function(event) {  
        var file = event.target.files[0];  
        var reader = new FileReader();  
        var check = document.getElementById('check').value;

        if (file != null) {
            reader.onload = function(event) {  
                var contents = event.target.result;  
                var update = document.getElementById("update");
                update.onclick = function()
                  {
                      update_to_server(contents , file.name , check);
                  }
              };  
        }
        
        reader.readAsArrayBuffer(file);
      }, false);
    auto_login();
    load_virus_list();
}