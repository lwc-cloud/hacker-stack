function load_index_text() {

    auto_login()

    var t = document.getElementById('t');
    var text_1 = `
Hacker Stack系统并不是一个界面效果或者是一个用来装逼的工具。而是一个真实
n
允许初学者利用n
各种可视化或者是半可是哈工具便可n
n
以完成渗透测试、社会工程学渗透、木马渗透、Web渗透等的综合性
n
在线渗透测试工具，主要是面向初学者快速的对网安进行学习和渗透测试操作，你在短时间内成为黑客
 n不是梦想
    `
    var splitText_1 = text_1.split('');

    var run = null;
    var i = 0;
    run = setInterval(function() {
        i++
        if (i + 1 == text_1.length) {
            clearInterval(run);
            return;
        }
        if (splitText_1[i] == "n") {
            t.innerHTML+= "<br />";
        }else {
            t.innerHTML+= "<a style='color:red'>"+splitText_1[i]+"</a>";
        }
    } , 100);
}

function auto_login() {
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
        } };
        xhr.onerror = function (e) {
            console.error(xhr.statusText);
        };
        xhr.send(null);
        } catch(a) {
            console.log(a)
        }
    return true
}