import apiClient from './apiClient';

// Sends signup request to backend, cookie is set automatically by the browser
export const signup = async (email, password) => {
    const response = await apiClient.post('/auth/signup', { email, password });
    return response.data;
};

// Sends login request to backend
export const login = async (email, password) => {
    const response = await apiClient.post('/auth/login', { email, password });
    return response.data;
};

// Redirects the browser to backend's Google OAuth2 endpoint
export const loginWithGoogle = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};