
import os
import json

class HackerStack_Config():
    _json_content = None
    def __init__(self):
        filename = 'resource/json/config.json'
        # 打开并读取JSON文件
        with open(filename, 'r', encoding='utf-8') as file:
            self._json_content = json.load(file)

    def get(self,json_object):
        return self._json_content[json_object]