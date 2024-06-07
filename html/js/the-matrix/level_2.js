

function check_renwu(values) {
    var whois = values[0].replaceAll(" ","").replaceAll("\n","");
    var dns = values[1].replaceAll(" ","").replaceAll("\n","");
    var nmap = values[2].replaceAll(" ","").replaceAll("\n","");
    var kaihu = values[3].replaceAll(" ","").replaceAll("\n","");
    var subdomain = values[4].replaceAll(" ","").replaceAll("\n","");
    var bug = values[5].replaceAll(" ","").replaceAll("\n","");
    var ip = values[6].replaceAll(" ","").replaceAll("\n","");
    
    console.log(whois == JSON.stringify(game_website_info['whois']).replaceAll(" ","").replaceAll("\n",""))
    console.log(dns == game_website_info['dns'].replaceAll(" ","").replaceAll("\n",""))
    console.log(nmap == game_website_info['nmap'].replaceAll(" ","").replaceAll("\n","") )
    console.log(kaihu == search_kaihu("王瑶一").replaceAll(" ","").replaceAll("\n",""))
    console.log(subdomain == game_website_info['subdomain'].replaceAll(" ","").replaceAll("\n",""))
    console.log(bug == game_website_info['bug'].replaceAll(" ","").replaceAll("\n",""))
    console.log(ip == game_website_info['IP'].replaceAll(" ","").replaceAll("\n",""))

    if (
        whois == JSON.stringify(game_website_info['whois']).replaceAll(" ","").replaceAll("\n","") &&
        dns == game_website_info['dns'].replaceAll(" ","").replaceAll("\n","") &&
        nmap == game_website_info['nmap'].replaceAll(" ","").replaceAll("\n","") &&
        kaihu == search_kaihu("王瑶一").replaceAll(" ","").replaceAll("\n","") &&
        subdomain == game_website_info['subdomain'].replaceAll(" ","").replaceAll("\n","") &&
        bug == game_website_info['bug'].replaceAll(" ","").replaceAll("\n","") &&
        ip == game_website_info['IP'].replaceAll(" ","").replaceAll("\n","")
    ) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET' , remote + "/ok_game/"+user+"/"+pwd+"/1" , false);
        xhr.send();
        showAlert("第二关已过，此关专门用于收集网站的信息安全技术信息，这些信息收集技术都是做信息安全渗透前常用的方法 (4秒后跳转下一关)" , null)
        setTimeout(() => {
            window.location.href = '';
        }, 4000);
    }
    else {
        showAlert("任务提交错误，可能是因为没有填写完整或者填写出错" , null)
    }
}