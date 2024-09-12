
import subprocess

def CommandSqlMap(host):
    information: str = ''
    host = str(host)
    if '|' in host or ';' in host or '&' in host or '$' in host or '`' in host or '>' in host or '<' in host or '*' in host or '?' in host or '[' in host or ']' in host or '{' in host or '}' in host or '!' in host or '@' in host or '#' in host or '%' in host or '^' in host or '~' in host or '|' in host or '\\' in host:
        return "NULL"
    if str(host).strip() == '':
        return 'NULL CHARSET '
    else:    
        # 构造Nmap命令
        nmap_command = ['sqlmap' , '-u', host , '--batch' , '--flush-session']

        # 使用subprocess.Popen执行命令
        process = subprocess.Popen(nmap_command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        # 获取命令行输出
        stdout, stderr = process.communicate()

        # 确保命令执行成功
        if process.returncode != 0:
            information += ("SqlMap扫描失败:") + "\n"
            information += (stderr.decode()) + "\n"
            return information

        # 打印或处理命令行输出
        information += ("Sqlmap扫描结果:") + '\n'
        information += (stdout.decode()) + '\n'
        return information
