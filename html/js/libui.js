function showAlert(message, waitTime) {
    var div = document.createElement('div');
    div.style.position = 'fixed';
    div.style.top = '0';
    div.style.right = '0';
    div.style.bottom = '0';
    div.style.left = '0';
    div.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    div.style.backdropFilter = 'blur(5px)';
    div.style.color = '#00FF00';
    div.style.fontFamily = 'sans-serif';
    div.style.fontSize = '16px';
    div.style.textShadow = '1px 1px 2px rgba(0, 0, 0, 0.3)';
    div.style.margin = '200px auto';
    div.style.maxWidth = '600px';
    div.style.overflow = 'auto';
    div.style.height = '300px'
    div.style.border = '3px solid #00FF00'

    var title = document.createElement('p');
    title.innerText = '提示';
    title.style.fontSize = '15px'
    title.style.width = "calc(100% - 20px)";
    title.style.margin = '0px'
    title.style.padding = '10px';
    title.style.color = "black"
    title.style.backgroundColor = "#00FF00"
    div.appendChild(title);

    var messageDiv = document.createElement('div');
    messageDiv.innerText = message;
    messageDiv.style.marginTop = '20px';
    messageDiv.style.padding = '10px';
    messageDiv.innerHTML = message;

    div.appendChild(messageDiv);
    
    var closeButton = document.createElement('button');
    closeButton.style.position = 'absolute';
    closeButton.style.bottom = '10px';
    closeButton.style.border = '1px solid green';
    closeButton.style.backgroundColor = 'transparent';
    closeButton.style.color = '#00FF00';
    closeButton.style.width='80px';
    closeButton.style.right = '10px'
    closeButton.innerText = "关闭";
    closeButton.onclick = function() {
        document.body.removeChild(div);
    };
    closeButton.onmouseenter=function(){
        closeButton.style.backgroundColor='#00FF00';
        closeButton.style.color='black'
    }
    closeButton.onmouseleave=function(){
        closeButton.style.backgroundColor='black';
        closeButton.style.color='green'
    }

    div.appendChild(closeButton);
    
    document.body.appendChild(div);
    
    if (waitTime != null) {
        setTimeout(function() {
            document.body.removeChild(div);
        }, waitTime);
    }
}

function showRightAlert(text, time) {
    // 创建警告框的容器
    var alertDiv = document.createElement('div');
    alertDiv.style.position = 'absolute';
    alertDiv.style.right = '10px';
    alertDiv.style.top = '10%';
    alertDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    alertDiv.style.backdropFilter = 'blur(5px)';
    alertDiv.style.color = 'white';
    alertDiv.style.padding = '16px';
    alertDiv.style.margin = '16px';
    alertDiv.style.borderRadius = '10px';
    alertDiv.style.boxShadow = '0 4px 8px rgba(0,0,0,0.1)';
    alertDiv.style.zIndex = '1000';
    alertDiv.style.width = '0px';
    alertDiv.style.fontSize = '16px';
    alertDiv.style.minHeight = '100px';
    alertDiv.style.transition = 'all 0.3s ease';
    

    var t = document.createElement("p");
    t.innerText = text;
    alertDiv.appendChild(t);
    t.style.display='none'

    var close = document.createElement('button');
    close.style.position = 'absolute';
    close.style.top = '10px';
    close.style.right = '10px';
    close.style.border = 'none';
    close.style.backgroundColor = 'transparent';
    close.style.color = 'white';
    close.style.padding = '4px';
    close.style.cursor = 'pointer';
    close.innerText = 'X';
    close.onclick = function() {
        document.body.removeChild(alertDiv);
    };
    alertDiv.appendChild(close);
    document.body.appendChild(alertDiv);

    setTimeout(() => {
            alertDiv.style.width = '200px';
    }, 1);
    setTimeout(() => {
        t.style.display='block'
    }, 300);

    // 设置定时器，在指定时间后移除警告框
    if (time != null)
    {
        setTimeout(function() {
            t.style.display='none'
            alertDiv.style.width = '0px';
            setTimeout(() => {
                document.body.removeChild(alertDiv);
            }, 300);
        }, time);
    }
}

// 使用示例
// showRightAlert('Hello, this is an alert!', 3000);
