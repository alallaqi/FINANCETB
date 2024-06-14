import React, { createContext, useContext, useState, useEffect } from 'react';
import axiosInstance from '../config/axiosConfig';
import Cookies from 'js-cookie';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setLoading(true);
            axiosInstance.get(`/api/users/profile/`)
                .then(response => {
                    setUser(response.data);
                    console.log('Auth state initialized with user data from API.');
                })
                .catch(err => {
                    console.error('Error fetching user details on init:', err);
                    logout();
                })
                .finally(() => setLoading(false));
        } else {
            console.log('No token found, user needs to log in.');
            setLoading(false);
        }
    }, []);

    const login = async (username, password) => {
        try {
            const response = await axiosInstance.post('/api/users/login', {
                username,
                password
            });
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));
            setUser(response.data.user);
            Cookies.set('user', JSON.stringify(response.data.user), { expires: 1 });
            console.log('Login successful, user:', response.data.user.username);
            return response;
        } catch (error) {
            console.error('Login failed:', error.response?.data);
            throw error;
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        Cookies.remove('user');
        setUser(null);
        delete axiosInstance.defaults.headers.common['Authorization'];
        console.log('User logged out successfully.');
    };

    const register = async (username, email, password) => {
        try {
            const response = await axiosInstance.post('/api/users/register', {
                username,
                email,
                password
            });
            return response.data;
        } catch (error) {
            throw error.response ? error.response.data : new Error('Network error');
        }
    };

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: !!user, loading, login, logout, register }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;