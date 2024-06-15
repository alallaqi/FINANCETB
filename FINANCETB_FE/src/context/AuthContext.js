import React, { createContext, useContext, useState, useEffect } from 'react';
import axiosInstance from '../config/axiosConfig';
import Cookies from 'js-cookie';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initializeAuth = async () => {
            const token = localStorage.getItem('token');
            console.log('Token retrieved from localStorage:', token);

            if (token) {
                setLoading(true);
                try {
                    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                    const response = await axiosInstance.get('/api/users/profile/');
                    if (response.data) {
                        setUser(response.data);
                        console.log('User data fetched:', response.data);
                    } else {
                        throw new Error("Failed to fetch user data");
                    }
                } catch (error) {
                    console.error('Failed to fetch user details or token invalid', error);
                    logout();
                } finally {
                    setLoading(false);
                }
            } else {
                console.log('No token found, user needs to log in.');
                setLoading(false);
            }
        };

        initializeAuth();
    }, []);

    const login = async (username, password) => {
        try {
            const response = await axiosInstance.post('/api/users/login', {
                username,
                password
            });
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));
            axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
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