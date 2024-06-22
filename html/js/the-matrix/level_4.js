
var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;

document.getElementById("file_update_1").style.display = 'block';

function check_renwu(values) {
    var virus_link = values[0].replaceAll(" ","").replaceAll("\n","");
    var virus_info = values[1].replaceAll(" ","").replaceAll("\n","");
    var virus_rm   = values[2].replaceAll(" ","").replaceAll("\n","");
    
}