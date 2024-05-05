


function update_to_server(contents , filename , check) {
    try{
        var toPath = remote+"/update_virus/"+filename+"/"+check;

        console.log(contents)
        var xhr = new XMLHttpRequest();
        xhr.open("POST",toPath,true);
        if (contents.byteLength > 1024 * 1024 * 10) {
            alert("上传zip文件大小不得大于 4 MB")
        }
        xhr.send(contents);
        console.log(xhr.responseText);
        showAlert("后台上传中 ... ..." , null)

        xhr.onload = function() {
            showAlert(xhr.responseText , null)
        }
    }catch(e) {
        alert(e);
    }
}

