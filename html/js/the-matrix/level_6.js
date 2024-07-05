
var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;

document.getElementById("file_update_2").style.display = 'block';

function check_renwu(values) {
    var IP_1 = values[0].replaceAll(" ","").replaceAll("\n","");
    var IP_2 = values[1].replaceAll(" ","").replaceAll("\n","");

    if (
        IP_1 == game_ip_location['154.201.114.114'].replaceAll(" ","").replaceAll("\n","") &&
        IP_2 == game_ip_location['101.114.514.191'].replaceAll(" ","").replaceAll("\n","")
    ) {
        showAlert("已过关，等待4秒进入下一关" , null);
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/5" , false);
        xhr.send();
        setTimeout(function() {
            window.location.href = '';
        }, 4000);
    } else {
        showAlert("任务中有选项错误，请重新检查" , null);
    }
}