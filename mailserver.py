from flask import Flask,request
import json
import threading
import time
import yagmail
import flask_cors
import random

app = Flask(__name__)
flask_cors.CORS(app)

visit_request = {}
mail_list = {}



@app.route('/api/v1/send_mail', methods=['POST','GET'])
def hello_world():
    try:
        data = request.get_data().decode('utf-8')
        print(data)
        json_data = json.loads(data)
        # 发送邮件
        send_to = json_data['send_to']
        subject = json_data['subject']
        content = json_data['content']

        # 连接服务器
        # 用户名、授权码、服务器地址
        use_mail = random_mail_key()
        yag_server = yagmail.SMTP(user=use_mail,
                password=mail_list[use_mail], smtp_ssl=True, host='smtp.163.com')

        # 发送对象列表
        email_to = [send_to]
        email_title = subject
        email_content = content
        # 发送邮件
        yag_server.send(email_to, email_title, email_content)
        # 关闭连接
        yag_server.close()
        return json.dumps({'status': 'ok','message': 'Send mail success.'}), 200
    except Exception as e:
        return json.dumps({'status': 'error','message': str(e)}), 500


def visit_request_threading() -> None:
    while True:
        time.sleep(60) # 休息 60 秒
        visit_request.clear()

def random_mail_key():
    mail_list_keys = list(mail_list.keys())
    random_key = random.choice(mail_list_keys)
    return random_key

if __name__ == '__main__':
    visit_request_thread = threading.Thread(target=visit_request_threading , name='visit_request')
    visit_request_thread.start()

    mail_list = json.load(open('mail_password.json', 'r'))
    print(mail_list)

    app.run(debug=False, host='0.0.0.0', port=11011)
