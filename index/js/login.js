// 154.9.253.147

function login() {
    var user = document.getElementById("user");
    var pwd = document.getElementById("pwd");

    var a = pwd.value;

    var t = document.getElementById("wait");
    t.style.display = "block"
        try{
            var xhr = new XMLHttpRequest();
            xhr.open("GET","http://154.9.253.147:8888/?Logon="+user.value+"?Passwd="+md5(pwd.value)+"?Command=list database",true)
            xhr.onload = function (e) {
                if (xhr.readyState === 4) {
                    var message = (xhr.responseText)
                    console.log(message)
                    if (message.indexOf("Passwd Or UserName Error!") == -1) {
                        document.cookie = '{"user":"'+user.value+'","pwd":"'+pwd.value+'"}'
                        window.location.href = "/";
                    }else{
                        alert(message)
                    }
                }else{
                    alert(e.message)
                }
              };
              xhr.onerror = function (e) {
                console.error(xhr.statusText);
              };
              xhr.send(null);
        } catch(a) {
            console.log(a)
        }
        return true
}
function reg() {
    var user = document.getElementById("user");
    var pwd = document.getElementById("pwd");
    var retype = document.getElementById("retype");

    var a = pwd.value;
    var b = retype.value;

    if (a.length < 8) {
        alert("密码不得小于 8 位字符");
        return false;
    }

    var t = document.getElementById("wait");
    t.style.display = "block"

    if (a == b) {
        try{
            var xhr = new XMLHttpRequest();
            xhr.open("GET","http://154.9.253.147:8888/?Logon=work?Passwd=c4d038b4bed09fdb1471ef51ec3a32cd?Command=sudo createUser "+user.value+" "+pwd.value,true)
            xhr.onload = function (e) {
                if (xhr.readyState === 4) {
                    var message = (xhr.responseText)
                    console.log(message)
                    if (message.indexOf("Create Successful!") != -1) {
                        document.cookie = '{"user":"'+user.value+'","pwd":"'+pwd.value+'"}'
                    }else{
                        alert(message)
                    }
                    window.location.href = "/";
                }else{
                    alert(e.message)
                }
              };
              xhr.onerror = function (e) {
                console.error(xhr.statusText);
              };
              xhr.send(null);
        } catch(a) {
            console.log(a)
        }
        return true
    }else{
        alert("确认密码和密码不一致")
        return false
    }
}