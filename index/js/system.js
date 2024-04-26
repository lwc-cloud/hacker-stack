
var remote = "http://127.0.0.1:5555";

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine";
}

function to_web_virus() {
    var i = document.getElementById('page');
    i.src = "./web_virus";
}

function social_attack(attack_type) {
    var user = JSON.parse(document.cookie).user;
    var pwd = JSON.parse(document.cookie).pwd;
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/"+attack_type+"/"+user+"/"+pwd,true);
    xhr.send();
    xhr.onload=function()
    {
        if (xhr.readyState === 4) {
            var response = xhr.responseText;
            showAlert("<h2 style='color:red'>收到信息:</h2>"+'请访问URL:<br/>' + remote + '/'+response , null)

            var run = setInterval(function() 
            {
                var get_message = new XMLHttpRequest();
                get_message.open(
                    'GET',remote+"/run/"+response , true
                    );
                get_message.send();
                get_message.onload = function() 
                {
                    if (get_message.readyState === 4) 
                    {
                        var r = get_message.responseText;
                        if (r.replace('\n','') != 'none')
                        {
                            showAlert("<h2 style='color:red'>收到信息:</h2><br />"+r, null)
                        }
                    }
                }
            } , 400); //使用短轮询，不是 websocket 用不起，而是短轮询更有性价比.
        }
    }
}
function web_clone() {
    var url = prompt('输入需要克隆的地址: (不要加 http://或https://)')
    if (url == null) {
        return;
    }
    else {
        var user = JSON.parse(document.cookie).user;
        var pwd = JSON.parse(document.cookie).pwd;
        var xhr = new XMLHttpRequest();
        xhr.open('GET',remote+"/WebClone/"+url+"/"+user+"/"+pwd , true);
        xhr.send();
        xhr.onload=function() {
            if (xhr.readyState == 4) {
                var response = xhr.responseText;
                console.log(response)
                showAlert('Please Visit URL: <a herf="_black">'+remote+'/w/'+response+'</a>' , null)
            }
        }
    }
}

function downloadBlob(data, fileName) {
    // 创建Blob对象
    const blob = new Blob([data]);
  
    // 创建临时URL
    const url = URL.createObjectURL(blob);
  
    // 创建a元素
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = fileName;
  
    // 将a元素添加到DOM中并模拟点击
    document.body.appendChild(a);
    a.click();
  
    // 移除a元素并撤销临时URL
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

function make_qrcode() {

    var url = prompt('请输入需要生成二维码的URL: ')
    if (url != null) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST' , remote+"/qr_code" , true);
        xhr.send(url);
        xhr.responseType = 'arraybuffer'; // 设置响应类型为arraybuffer
        xhr.onload = function() {
            downloadBlob(xhr.response, url+".png");
        }
    }
}

function create_web_virus() {
    
}