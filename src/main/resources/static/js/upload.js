async function uploadToServer (formObj) {

    console.log("upload to server......")
    const response = await axios({
        method: 'post',
        url: '/upload',
        data: formObj,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });

    /*
    const response = await axios.post('/upload',
        formObj,
        { // 객체 리터럴(key-value)
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        }
    );
    */

    console.log('여기는 upload.js', response.data)

    return response.data
}

async function removeFileToServer(uuid, fileName){

    const response = await axios.delete(`/remove/${uuid}_${fileName}`)

    return response.data

}
