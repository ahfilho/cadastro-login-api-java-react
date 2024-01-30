import React from 'react';
import axios from 'axios';
const getToken = () => {
    return localStorage.getItem('USER_KEY');
}

export const userLogin = (authRequest) => {
    return axios({
        method: 'POST',
        url: `${process.env.hostUrl || 'http://localhost:8080'}/user/auth/login`,
        data: authRequest
    });
}

export const fetchUserData = () => {
    const token = localStorage.getItem('USER_KEY');

    // Verifica se o token está presente
    if (token) {
        return axios({
            method: 'GET',
            url: `${process.env.hostUrl || 'http://localhost:8080'}/user/auth/userinfo`,
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
    } else {
        window.location.href = '/login';
        
        return Promise.reject(new Error('Token não disponível'));
    }
}
