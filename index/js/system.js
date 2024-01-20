var remote = 'http://127.0.0.1:5555'

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine";
}

function qq_attack() {
    var user = JSON.parse(document.cookie).user;
    var pwd = JSON.parse(document.cookie).pwd;
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/qq/"+user+"/"+pwd,true);
    xhr.send();
    xhr.onload=function(){
        if (xhr.readyState === 4) {
            showAlert(xhr.response , null)
        }
    }
}