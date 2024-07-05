
var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;

document.getElementById("file_update_1").style.display = 'block';

function getinfo() {
    return "[ navigator ] /n" +
    "Platform: "+navigator.platform + "/n" +
    "UserAgent: " + navigator.userAgent + '/n' +
    "Language: " + navigator.language + "/n" +
    "Geolocation: " + navigator.geolocation + '/n' +
    "OnLine: " + navigator.onLine + "/n" +
    "AppName: "+navigator.appName + '/n' +
    "CookieEnabled: " + navigator.cookieEnabled
}

function check_renwu(values) {
    var virus_link = values[0].replaceAll(" ","").replaceAll("\n","");
    var virus_info = values[1].replaceAll(" ","").replaceAll("\n","");
    var virus_rm   = values[2].replaceAll(" ","").replaceAll("\n","");

    console.log(String(virus_link).startsWith("http://api.hackerstack.top/"))
    console.log(virus_info == String(getinfo()).replaceAll(" ","").replaceAll("\n",""))
    console.log(virus_rm == "[INFO] SYSTEM COMMAND RUN SUCCESSFUL.".replaceAll(" ","").replaceAll("\n",""))
    console.log(virus_rm)
    if (
        String(virus_link).startsWith("http://api.hackerstack.top/") &&
        virus_info == String(getinfo()).replaceAll(" ","").replaceAll("\n","") &&
        virus_rm == "[INFO] SYSTEM COMMAND RUN SUCCESSFUL.".replaceAll(" ","").replaceAll("\n","")
    ) {
        showAlert("已过关，我们成功摧毁了他们的服务器计算机系统，等待4秒进入下一关" , null);
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/3" , false);
        xhr.send();
        setTimeout(function() {
            window.location.href = '';
        }, 4000);
    } else {
        showAlert("任务中有选项错误，请重新检查" , null);
    }
}