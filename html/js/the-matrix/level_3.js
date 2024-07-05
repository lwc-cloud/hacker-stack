
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

var plan_game = `
发展计划:
1. 1年内赚取 1000 万
2. 与澳门新葡京联合出品更多的狼友产品：狼友贷款。专门给那些需要钱嫖娼的的狼友，要求的就是利息高，放贷快，门槛低，最高 5000，差不多了。
3. 澳门新葡京给了我们一定的投资和合作，反正都是合法的他们，要求说叫我们在软件里面更多的加入他们的产品功能，以及引诱到澳门去赌博。
每引导一个人去他们的平台上消费，就可以拿到 100 块钱的佣金，这好啊。
4. 那个狼友贷款，新葡京给了我们 1000万搞。依靠我们的体量优势，肯定能够赚的盆满钵满。
`;

var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;
var put = document.getElementById('chat_content_2');
var loop=null;
var i = 0;

showAlert("<h3 style='color: red'>信息: </h3>正在利用 AI 模块自动化社会工程学诱导。本关卡在收集用户名称这块限制世间 40秒" , 3000)

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

var time_wait_1 = chat_content.length * 2000 + 10000;
var time_wait_2 = 1000

setTimeout(function(){
    document.getElementById('qq_window_2').style.display = 'none';
    document.getElementById('qq_window_3').style.display = 'block';
}, time_wait_1);

function check_renwu(values) {
    var socialEngine_link = values[0].replaceAll(" ","").replaceAll("\n","");
    var mail_get = values[1].replaceAll(" ","").replaceAll("\n","");
    var password = values[2].replaceAll(" ","").replaceAll("\n","");
    var friends = values[3].replaceAll(" ","").replaceAll("，",",").replaceAll("\n","").split(",");
    var plan = values[4].replaceAll(" ","").replaceAll("\n","");
    
    function check_user_ok(kaihu_list) {
        var user_list = [
            "彪哥",
            "老胡",
            "开发小王",
            "老瑶姐"
        ]
        var ok_number = 0;
        for (var key in kaihu_list) {
            var user = kaihu_list[key];
            if (user_list.includes(user)) {
                ok_number += 1;
                continue;
            } else {
                showAlert("第四项任务中用户名称中有填写错误" , null)
                return false;
            }
        }
        return ok_number == 4;
    }

    var ok = false;

    if (String(socialEngine_link).startsWith("http://api.hackerstack.top/") != true) {
        showAlert("第一项任务错误: 生成的链接错误" , null)
    }
    else if (
        mail_get == 'sdf890324j3j24900i123jkl1nj23lkhjrsoidujfpoojq23' &&
        password == 'a114loveyou' &&
        check_user_ok(friends) &&
        plan == plan_game.replaceAll(" ","").replaceAll("\n","")
    ) {
        showAlert("已过关: 本关利用了 社会工程学，来盗取他们的QQ号码，这些都是真实操作。4秒后进入下一关" , null);
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/2" , false);
        xhr.send();
        setTimeout(function() {
            window.location.href = '';
        }, 4000);
    } else {
        showAlert("任务中有填写错误" , null)
    }
}