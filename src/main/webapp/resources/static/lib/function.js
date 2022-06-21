// 널 체크 함수
function isEmpty(value){
    if(
        value == "" || value == null || value == undefined ||
        value == "null" || 
        ( value != null && typeof value == "object" && 
        !Object.keys(value).length )
    )
    {
        return true;
    }
    else {
        return false;
    }
}