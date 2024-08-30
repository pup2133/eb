import axios from "axios";

/**
 * 1. axios instance 생성
 * @type {axios.AxiosInstance}
 */
export const instance = axios.create({
    baseURL: 'http://localhost:8080/api', // 기본 URL
});

/**
 * 1. 에러 처리를 위해 interceptors 사용
 * 2. 에러가 발생할 경우 에러의 메세지를 alert으로 사용자에게 전달
 */
instance.interceptors.response.use(response => response, error => {
    if (error.response) {
        alert(error.response.data.message);
    }
    return Promise.reject(error);
});