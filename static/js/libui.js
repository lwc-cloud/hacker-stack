function showAlert(htmlContent) {
    // 创建模态框的容器
    const modal = document.createElement('div');
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100%';
    modal.style.height = '100%';
    modal.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    modal.style.display = 'flex';
    modal.style.justifyContent = 'center';
    modal.style.alignItems = 'center';

    // 创建模态框的内容区域
    const content = document.createElement('div');
    content.style.backgroundColor = '#fff';
    content.style.padding = '20px';
    content.style.borderRadius = '5px';
    content.style.width = '50%';
    content.style.maxWidth = '600px';
    content.innerHTML = htmlContent+"<br />";

    // 创建关闭按钮
    const closeButton = document.createElement('button');
    closeButton.textContent = '关闭';
    closeButton.style.marginTop = '20px';
    closeButton.style.padding = '5px 10px';
    closeButton.style.backgroundColor = '#f44336';
    closeButton.style.color = '#fff';
    closeButton.style.border = 'none';
    closeButton.style.borderRadius = '4px';
    closeButton.style.cursor = 'pointer';

    // 关闭按钮的点击事件
    closeButton.addEventListener('click', () => {
      modal.remove();
    });

    // 将关闭按钮添加到内容区域
    content.appendChild(closeButton);

    // 将内容区域添加到模态框
    modal.appendChild(content);

    // 将模态框添加到页面
    document.body.appendChild(modal);
  }