import axios from 'axios';

// Base Axios instance configured to talk to our Spring Boot backend
const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true, // sends the httpOnly JWT cookie with every request
    headers: {
        'Content-Type': 'application/json',
    },
});

export default apiClient;