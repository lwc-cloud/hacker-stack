
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