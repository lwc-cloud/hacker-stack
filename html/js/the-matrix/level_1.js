
var chat_content = [
    "系统: 欢迎用户 z189123 入群",
    "张三: 看看逼",
    "李四: [图片]",
    "李四: [图片]",
    "王五: 真好啊，妹妹多大",
    "李四: 19，大学生，约么?",
    "赵六: 这么漂亮，多少钱一晚",
    "王五: 我出 3000，陪我一整天。",
    "李四: 太少，至少 6000 起步",
    "张一一: [笑脸]",
    "用户9289289s: [笑脸]",
    "用户1232133: 多发几张",
    "接待员瑶瑶: [图片] [图片] [图片]",
    "接待员瑶瑶: 今日福利哦，现在充值我们网站的会员，价格更低",
    "接待员瑶瑶: 现在每个月只需要 39.9 就能够成为我们的会员了",
    "接待员瑶瑶: [视频: 男人的天堂]",
    "用户000011: 我想要接待员瑶瑶",
    "接待员瑶瑶: QQ 1145141919",
    "王五: 我忘记网站网址了",
    "接待员瑶瑶: www.ac123ff.com",
    "张三: 我还是想要学生妹，现在女大学生不错",
    "用户582432: 我是下北泽艺术学院的女生，这是我的学生证 [图片],等你来玩哦",
    "张三: 价格多少?",
    "用户582432: 艺术生贵一点,2000一晚",
    "张三: 行,妹妹多发点涩图",
    "用户582432: [图片] [图片]",
    "张三: 逼毛好多啊，我喜欢",
    "系统: 欢迎用户 m12389922 入群",
    "系统: 欢迎 张喔喔 入群",
    "赵六: 能再发点么，我爱看，一天导 3 发了要",
    "接待员瑶瑶: [图片]",
    "接待员瑶瑶: [图片]",
    "接待员瑶瑶: 我只能够发这么多了哦，更多的加我QQ冲会员哦 [笑脸]",
    "接待员瑶瑶: q 1145141919"
]

var json = JSON.parse(document.cookie);
var user = json.user;
var pwd = json.pwd;
var put = document.getElementById('chat_content');
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
} , 1000)

function check_renwu(values) {
    var title = values[0].replaceAll(" ","").replaceAll("\n","");
    var qq = values[1].replaceAll(" ","").replaceAll("\n","");
    var kaihu = values[2].replaceAll(" ","").replaceAll("\n","");
    var users = String(values[3]).replaceAll("，",",").split(',');
    var website = values[4].replaceAll(" ","").replaceAll("\n","");
    
    function check_user_ok(kaihu_list) {
        var user_list = [
            "张三",
            "接待员瑶瑶",
            "李四",
            "王五",
            "赵六",
            "用户582432",
            "张一一",
            "用户000011",
            "用户1232133",
            "用户9289289s"
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
        return ok_number == 10;
    }

    var ok = false;
    console.log(title == "同城狼友交流群(21238)")
    console.log(qq == "1145141919")
    console.log(kaihu == search_kaihu("接待员瑶瑶").replaceAll("\n","").replaceAll(" ",""));
    console.log(check_user_ok(users))
    console.log(website == 'www.ac123ff.com')
    if (
        title == "同城狼友交流群(21238)" &&
        qq == "1145141919" &&
        kaihu == search_kaihu("接待员瑶瑶").replaceAll("\n","").replaceAll(" ","") &&
        check_user_ok(users) &&
        website == 'www.ac123ff.com'
    ) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/0" , false);
        xhr.send();
        showAlert("第一关已过，恭喜你，学会了基础的通过聊天软件收集信息，这些虽然看上去很繁琐或者无聊，但也是渗透中不可或缺的一环。等待 4秒 切换" , null);
        setTimeout(function() {
            window.location.href = '';
        }, 4000);
    } else {
        showAlert("任务提交错误，可能是因为没有填写完整或者填写出错" , null)
    }
    
}