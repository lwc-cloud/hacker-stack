
var kaihu_db = {
    "张三" : "1. 上海普陀真南路114号514弄\n2. 年龄: 20\n3. 身份证号码: 31011700001010101\n职业: 无业游民",
    "李四" : "1. 下北泽赢梦东路10号\n2. 年龄: 19\n3. 身份证号码: 未知\n4. 职业: 学生",
    "王五" : "1. 上海静安区共和新路200号114514弄\n2.年龄: 30\n3. 身份证号码: 31010166682378123\n职业: 程序员",
    "接待员瑶瑶" : "1. 湖南长沙\n2. 年龄: 40\n3. 身份证号码: 24021010101011111\n4. 职业: 未知",
    "赵六" : "1. 上海宝山区镜泊湖路100号114514弄\n2. 年龄: 32\n3. 身份证号码: 3101130224412322\n职业: HR",
    "王瑶一" : "1. 深圳南山区114路51号\n2. 年龄: 35\n3. 身份证号码: 未知\n职业: 未知"
}

function kaihu() {
    var message = document.getElementById('message').value;
    var console = document.getElementById('console');

    var get_db = kaihu_db[message];
    if (get_db == null) {
        showAlert("没有查询到该用户" , null);
    }
    else {
        console.innerHTML = String(get_db).replaceAll("\n","<br />");
    }
}

function search_kaihu(message) {
    return kaihu_db[message];
}