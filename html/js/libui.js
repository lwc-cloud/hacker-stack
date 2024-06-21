function showAlert(message, waitTime) {
    var div = document.createElement('div');
    div.style.position = 'fixed';
    div.style.top = '0';
    div.style.right = '0';
    div.style.bottom = '0';
    div.style.left = '0';
    div.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    div.style.color = '#00FF00';
    div.style.fontFamily = 'sans-serif';
    div.style.fontSize = '16px';
    div.style.textShadow = '1px 1px 2px rgba(0, 0, 0, 0.3)';
    div.style.padding = '20px';
    div.style.margin = '200px auto';
    div.style.maxWidth = '600px';
    div.style.overflow = 'auto';
    div.style.height = '300px'
    div.style.border='1px solid #00FF00'
    div.innerHTML = message;
    
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