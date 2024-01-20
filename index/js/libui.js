function showAlert(message, waitTime) {
    // 创建弹窗元素
    var alertBox = document.createElement('div');
    // 设置一些基本样式
    alertBox.style.position = 'fixed';
    alertBox.style.width='500px';
    alertBox.style.height='300px';
    alertBox.style.top = '50%';
    alertBox.style.left = '50%';
    alertBox.style.border='1px solid green';
    alertBox.style.color='green';
    alertBox.style.transform = 'translate(-50%, -50%)';
    alertBox.style.backgroundColor = 'rgba(0, 0, 0, 0.678)';
    alertBox.style.padding = '20px';
    alertBox.style.borderRadius = '5px';
    alertBox.style.boxShadow = '0 2px 10px rgba(0,0,0,0.5)';
    
    // 设置消息内容
    var messageText = document.createTextNode(message);
    alertBox.appendChild(messageText);
    // 创建关闭按钮
    var closeButton = document.createElement('button');
    closeButton.textContent = '关闭';
    closeButton.style.color='green'
    closeButton.style.padding = '5px 10px';
    closeButton.style.marginTop = '240px';
    closeButton.style.border = '1px solid green';
    closeButton.style.background = 'black';
    closeButton.style.color = 'green';
    closeButton.style.borderRadius = '5px';
    closeButton.style.cursor = 'pointer';

    closeButton.onmouseenter = function() {
        closeButton.style.background = 'green';
        closeButton.style.color='black'
    }
    closeButton.onmouseleave = function() {
        closeButton.style.color = 'green';
        closeButton.style.background = 'black';
    }
    
    // 为关闭按钮添加点击事件
    closeButton.onclick = function() {
      document.body.removeChild(alertBox);
    };
    // 将关闭按钮添加到弹窗中
    alertBox.appendChild(closeButton);
    // 将弹窗添加到body中
    document.body.appendChild(alertBox);

    if (waitTime != null) {
        setTimeout(function() {
            document.body.removeChild(alertBox);
        } , waitTime);
    } 
  }
  // 使用函数
  showAlert('这是一个自定义的弹窗消息！');