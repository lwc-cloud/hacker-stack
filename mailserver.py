from flask import Flask,request
import json
import threading
import time
import subprocess
import flask_cors

app = Flask(__name__)
flask_cors.CORS(app)

visit_request = {}
@app.route('/api/v1/send_mail', methods=['POST','GET'])
def hello_world():
    # 限制每个IP对制定api的访问.
    client_ip = request.remote_addr
    if client_ip in visit_request:
        visit_request[client_ip] = visit_request[client_ip] + 1
        if visit_request[client_ip] >= 4:
            return json.dumps({'status': 'error','message': 'Too many requests'}), 429
    else:
        visit_request[client_ip] = 1

    data = request.get_data().decode('utf-8')
    print(data)
    json_data = json.loads(data)
    # 发送邮件
    send_to = json_data['send_to']
    subject = json_data['subject']
    content = json_data['content']

    from_field = " Linwinsoft账户"
    command = f"echo '{content}' | mail -s '{subject}' {send_to}"
    result = subprocess.run(command, shell=True, check=True, text=True, capture_output=True)
    if result.returncode == 0:
        print("邮件发送成功")
        return json.dumps({'status': 'ok','message': '邮件发送成功'}), 200
    else:
        print("邮件发送失败，返回码：", result.returncode)
        return json.dumps({'status': 'error','message': '邮件发送失败'}), 500


def visit_request_threading() -> None:
    while True:
        time.sleep(60) # 休息 60 秒
        visit_request.clear()

if __name__ == '__main__':
    visit_request_thread = threading.Thread(target=visit_request_threading , name='visit_request')
    visit_request_thread.start()

    app.run(debug=False, host='0.0.0.0', port=11011)