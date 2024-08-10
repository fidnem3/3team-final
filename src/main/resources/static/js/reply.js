async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`)
    //console.log(result)
    return result;
}

// getList 버전 #1
/*
 async function getList({bno, page, size, goLast}){
    console.log("여기는 reply.js의 getList")
     const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})
     return result.data
 }
*/

// getList 버전 #2
async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        alert('다시 getList 호출 : bno/lastPage/size : ' + bno + " " + lastPage)
        return getList({bno:bno, page:lastPage, size:size})
    }
    return result.data
}

// 댓글 추가
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj)
    return response.data
}

// 댓글 한개 조회
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

// 댓글 수정
async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

// 댓글 삭제
async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}
