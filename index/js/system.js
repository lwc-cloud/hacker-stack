var remote = 'http://127.0.0.1:5555'

function to_social() {
    var i = document.getElementById('page');
    i.src = "./socialEngine";
}

function social_attack(attack_type) {
    var user = JSON.parse(document.cookie).user;
    var pwd = JSON.parse(document.cookie).pwd;
    var xhr = new XMLHttpRequest();
    xhr.open("GET",remote+"/api/"+attack_type+"/"+user+"/"+pwd,true);
    xhr.send();
    xhr.onload=function(){
        if (xhr.readyState === 4) {
            var response = xhr.responseText;
            showAlert('Please Visit URL: ' + remote + '/'+response , null)

            var run = setInterval(function() {
                var get_message = new XMLHttpRequest();
                get_message.open('GET',remote+"/run/"+response , true);
                get_message.send();
                get_message.onload = function() {
                    if (get_message.readyState === 4) {
                        var r = get_message.responseText;
                        if (r.replace('\n','') != 'none') {
                            showAlert("<h2 style='color:red'>收到信息:</h2>"+r, null)
                        }
                    }
                }
            } , 400);
        }
    }
}