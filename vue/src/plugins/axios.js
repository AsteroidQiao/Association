"use strict";

import Vue from 'vue';
import axios from "axios";
import {serverIp} from "../../public/config";
import {Message} from 'element-ui';
import router, {resetRouter} from "@/router";
// vue/node_modules/vue-router/types/router.d.ts:37
// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

let config = {
    // baseURL: process.env.baseURL || process.env.apiUrl || ""
    baseURL: "http://" + serverIp + ":9090/"
    // timeout: 60 * 1000, // Timeout
    // withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

_axios.interceptors.request.use(
    function (config) {
        // Do something before request is sent
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
        if (user) {
            config.headers['token'] = user.token;  // 设置请求头token
            // config.headers['referer'] = 'https://gitee.com';  // 设置请求头token
        }
        return config;
    },
    function (error) {
        // Do something with request error
        return Promise.reject(error);
    }
);

// Add a response interceptor
_axios.interceptors.response.use(
    function (response) {
        // Do something with response data
        if (response.data.code === 401) {
            //加了提示
            Message({
                showClose: true,
                message: response.data.msg,
                type: 'error'
            });
            // alert(response.data.msg)
            router.push('/login')
        }
        return response;
    },
    function (error) {
        // Do something with response error
        return Promise.reject(error);
    }
);

Plugin.install = function (Vue, options) {
    Vue.axios = _axios;
    window.axios = _axios;
    Object.defineProperties(Vue.prototype, {
        axios: {
            get() {
                return _axios;
            }
        },
        $axios: {
            get() {
                return _axios;
            }
        },
    });
};

Vue.use(Plugin)

export default Plugin;
