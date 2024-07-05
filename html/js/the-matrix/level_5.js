
var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;

document.getElementById("file_update_1").style.display = 'block';

function getinfo() {
    var info = "[ navigator ] /n" +
            "Platform: "+navigator.platform + "/n" +
            "UserAgent: " + navigator.userAgent + '/n' +
            "Language: " + navigator.language + "/n" +
            "Geolocation: " + navigator.geolocation + '/n' +
            "OnLine: " + navigator.onLine + "/n" +
            "AppName: "+navigator.appName + '/n' +
            "CookieEnabled: " + navigator.cookieEnabled;
    return info;
}

function check_renwu(values) {
    var virus_link = values[0].replaceAll(" ","").replaceAll("\n","");
    var virus_get = values[1].replaceAll(" ","").replaceAll("\n","");
    var virus_info = values[2].replaceAll(" ","").replaceAll("\n","");
    var kaihu = values[3].replaceAll(" ","").replaceAll("\n","");

    console.log(String(virus_link).startsWith("http://api.hackerstack.top/"))
    console.log(virus_get == "sdf890324j3j24900i123jkl1nj23lkhjrsoidujfpoojq23")
    console.log(virus_info == String(getinfo()).replaceAll(" ","").replaceAll("\n",""))
    console.log(kaihu == search_kaihu("彪哥").replaceAll(" ","").replaceAll("\n",""))

    if (
        String(virus_link).startsWith("http://api.hackerstack.top/") &&
        virus_get == "sdf890324j3j24900i123jkl1nj23lkhjrsoidujfpoojq23" &&
        virus_info == String(getinfo()).replaceAll(" ","").replaceAll("\n","") &&
        kaihu == search_kaihu("彪哥").replaceAll(" ","").replaceAll("\n","")
    ) {
        showAlert("已过关，等待4秒进入下一关" , null);
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/4" , false);
        xhr.send();
        setTimeout(function() {
            window.location.href = '';
        }, 4000);
    } else {
        showAlert("任务中有选项错误，请重新检查" , null);
    }
}