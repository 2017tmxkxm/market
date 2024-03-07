// 문자열 받침 포함 여부 확인
function hasLastConsonantLetter(value) {
    return ((value.charCodeAt(value.length - 1) -0xAC00) % 28) > 0;
}

// 필드 유효성 검사
function isValid(target, fieldName, focusTarget) {
    if(target.value.trim()) {
        console.log("target.value.trim() : "+ target.value.trim())
        return true;
    }

    const particle = (hasLastConsonantLetter(fieldName)) ? '을' : '를';

    alert(`${fieldName + particle} 입력해 주세요.`);
    ( !focusTarget ? target : focusTarget).focus();
    throw new Error(`"${target.id}" is required...`)
}

// 데이터 조회
function getJson(uri, params) {
    let json = {}

    $.ajax({
        url: uri,
        type: 'get',
        dataType: 'json',
        data: params,
        async: false,
        success: function (response) {
            json = response;
        },
        error: function(request, status, error) {
            console.log(error)
        }
    })

    return json;
}

// 데이터 저장
function callApi(uri, method, params) {

    let json = {}

    $.ajax({
        url: uri,
        type: method,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: (params) ? JSON.stringify(params) : {},
        async: false,
        success: function(response) {
            json = response;
        },
        error: function(request, status, error) {
            console.log(error);
        }
    })

    return json;
}