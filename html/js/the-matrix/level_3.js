
var chat_content = [
    "接待员瑶瑶: 哥，你来是什么事情，找我",
    "(AI) 开发小王: 是这样的，给你们外包的这个网站，最近被黑客攻击了，现在有很严重的安全问题",
    "(AI) 开发小王: 就是这个网站当时我们给你们开发的时候不是因为要接受一个QQ支付的接口嘛,现在出现了安全问题，需要你重新登录一下防止被盗",
    "(AI) 开发小王: qq支付呢目前呢如果说不现在进行任何安全验证的话可能两天后强制下线，不允许用的",
    "接待员瑶瑶: 这么严重 [震惊]",
    "接待员瑶瑶: 待会哈",
    "接待员瑶瑶: 我和彪哥说一说",
    "(AI) 开发小王: 对了，你们这个网站服务的话外包维护还要继续么，毕竟用户这么多，广大狼友需求量这么大，要是真的出现了什么问题损失可不止一点点",
    "接待员瑶瑶: 可以的，我现在就去验证一下，现在人挺多的，40来万个用户，彪哥现在都不怎么管事情了",
    "接待员瑶瑶: 彪哥刚刚和我说了,之后整个狼友群以及平台的业务交给我了，他好好在台湾享受",
    "(AI) 开发小王: 好的，合作愉快"
]
var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;
var put = document.getElementById('chat_content_2');
var loop=null;
var i = 0;
loop = setInterval(function() {
    var chat_obj = document.createElement('div');
    if (i + 1 == chat_content.length) {
        clearInterval(loop);
    }
    chat_obj.innerText = chat_content[i];
    chat_obj.style.width = '90%';
    chat_obj.style.height = 'auto';
    chat_obj.style.padding = '5px';
    chat_obj.style.borderRadius = '10px';
    chat_obj.style.backgroundColor = 'rgb(228, 228, 228)';
    chat_obj.style.border = 'none';
    chat_obj.style.marginTop = '10px';
    chat_obj.style.textAlign = 'left';
    put.appendChild(chat_obj);
    i++;
} , 2000)

setTimeout(function(){
    document.getElementById('qq_window_2').style.display = 'none';
    document.getElementById('qq_window_3').style.display = 'block';
}, 1 * 1000);