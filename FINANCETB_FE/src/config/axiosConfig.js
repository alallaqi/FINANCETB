import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'https://boiling-lowlands-43453-ff95b478022d.herokuapp.com',
  headers: {
      'Content-Type': 'application/json'
  }
});

// Intercept every request and add the token to the headers
axiosInstance.interceptors.request.use(function (config) {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, function (error) {
  return Promise.reject(error);
});

export default axiosInstance;