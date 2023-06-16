import router from "@/router";
import axios from "axios";

// 공통으로 사용하는 유틸함수들 모음집
// vue 파일에서 사용할때는 전역함수처럼 this.$함수명 이런식으로 사용 가능

export default {
    install(app) {
        console.log(app)
        const globalProperties = app?.config.globalProperties
        globalProperties.$axiosSend = commonAxios.axiosSend;
        globalProperties.$routerPush = commonRouter.routerPush;
        globalProperties.$isNotEmpty = commonValid.isNotEmpty;
        globalProperties.$isNotEmptyObj = commonValid.isNotEmptyObj;
        globalProperties.$isNotEmptyArray = commonValid.isNotEmptyArray;
        globalProperties.$dateFormat = commonDate.dateFormat;
        // globalProperties.$convertFullDate = commonConvert.convertFullDate;
        // globalProperties.$axiosSend = commonAxios.axiosSend;
        // globalProperties.$axiosSend = commonAxios.axiosSend;

        
    }
}

// axios함수들
export const commonAxios = {
    // axios (method(필수값),보낼주소(필수값), 보낼 파라미터)
    // 호출예시 : this.$axiosSend('get', '/api/something', {name: '123', value: '456'}, true)
    // header = {'cont-type' : '필요한거'}
    axiosSend(method, url, params, headers) {
        console.log('Axios ready : method = %o, url = %o', method, url)
        return axios({
            method,
            url, 
            params,
            headers
        })
    }
}

// router함수들
export const commonRouter = {
    // router push함수 url: 라우트 주소, query: 라우트에 태워 보낼 데이터, hasData: true: 보낼 데이터값 있음(기본), false: 주소로 호출만 함) 
    // 호출예시 : this.$routerPush('/url', {name: '123', value: '456'}, true)
    routerPush: (url, query, hasData) => {
        console.log('router push ::: url = %o, query = %o, hasData = %o', url, query, hasData)
        // 1. router에 담아 보낼 데이터가 있다면
        if (hasData) {
            router.push({
                name: url,
                query
            })
        } else { // 2. router에 담아 보낼 데이터가 없이 router호출만
            router.push(url)
        }
    }
}

// 인피니티스크롤 공통쪽으로 개발
export const commonScroll = {

}



// 공통 밸리데이션
export const commonValid = {
    // 빈 데이터 감지용
    isNotEmpty: (value) => {
        // console.log('isNotEmpty ::: ', value)
        if (value === '') return false
        if (value === null) return false
        if (value === undefined) return false
        // obj 및 array형식감지는 새로운 함수로 대체
        // if (value === {}) return false
        // if (Array?.isArray(value) && value === []) return false
        return true
    },
    // obj데이터가 빈값이 아닌지
    isNotEmptyObj : (objValue) => {
        // console.log("isNotEmptyObj ::: " , objValue)
        // 받아온 데이터 타입이 obj일경우
        if (typeof objValue === 'object') {
            // console.log('obj ::: ', typeof objValue === 'object')
            // 받아온값이 빈값이면 false 반환
            if ((objValue === '') || (objValue === null) || (objValue === undefined) || (objValue === {}) || (Object.keys(objValue).length === 0)){
                return false
            }
        } else {
            // 받아온 데이터가 obj형태가 아니면 false 반환
            return false 
        }
        return true
    },
    // array데이터가 빈값이 아닌지
    isNotEmptyArray : (arrayValue) => {
        // console.log("isNotEmptyArray ::: " , arrayValue)
        // 1. 받아온 데이터 타입이 array일경우
        // eslint-disable-next-line valid-typeof
        if (Array.isArray(arrayValue)) {
            // console.log('array ::: ', Array.isArray(arrayValue))
            // 2. 받아온값이 빈값이면 false 반환
            if ((arrayValue === '') || (arrayValue === null) || (arrayValue === undefined) || arrayValue === []){
                return false
            }
        } else {
            // 받아온 데이터가 array형태가 아니면 false반환
            return false
        }
        return true
    }
}

//날짜값 변경해주는 함수
export const commonDate = {
    dateFormat : (value) => {
        console.log('dateFormat ::: ', value)
        var date = new Date(value)

        //년,월,일
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var day = date.getDate()

        //월 1자리일 경우 앞에 0 붙임
        if (month < 10) {
            month = "0" + month
        }
        //일 1자리일 경우에 앞에 0 붙임
        if (day < 10){
            day = "0" + day
        }
        return year + "-" + month + "-" + day
    }
}
