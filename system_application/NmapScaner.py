import nmap

def getNmap(host):
    information: str = ''
    # 创建一个nmap对象
    nm = nmap.PortScanner()

    # 执行扫描，这里使用-A参数来获取更多信息，-p-表示扫描所有端口（1-65535）
    nm.scan(host, arguments='-A -p-')

    # 获取扫描的主机信息
    host_info = nm.all_hosts()

    # 确保主机信息不为空
    if host_info:
        # 获取第一个主机（通常只有一个主机）
        host = list(host_info)[0]

        # 获取每个主机的所有协议
        for proto in nm[host].all_protocols():
            information += (f'Protocol: {proto}')

            # 获取该协议下的所有端口
            lport = nm[host][proto].keys()
            sorted(lport)

            # 遍历每个端口
            for port in lport:
                information += (f'port: {port}\tstate: {nm[host][proto][port]["state"]}') + '\n'

                # 如果端口是开放的，可以获取更多服务信息
                if nm[host][proto][port]["state"] == "open":
                    service_info = nm[host][proto][port]
                    for key, value in service_info.items():
                        information += (f'{key}: {value}') + '\n'

    return information