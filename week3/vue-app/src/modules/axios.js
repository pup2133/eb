import axios from "axios";

export const instance = axios.create({
    baseURL: 'http://localhost:8080/api', // 기본 URL
});

instance.interceptors.response.use(response => response, error => {
    if (error.response) {
        alert(error.response.data.message);
    }
    return Promise.reject(error);
});