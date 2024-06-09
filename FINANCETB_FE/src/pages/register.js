import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';
import { useAuth } from '../context/AuthContext'; // Ensure this path is correct to import your AuthContext
import styles from '../styles/Auth.module.css';

const SignupPage = () => {
    const [formState, setFormState] = useState({
        username: '',
        email: '',
        password: '',
    });
    const [backgroundImage, setBackgroundImage] = useState('');
    const [alert, setAlert] = useState('');
    const { register } = useAuth(); // Use register function from AuthContext
    const router = useRouter();

    useEffect(() => {
        const fetchBackgroundImage = async () => {
            try {
                const response = await axios.get('https://source.unsplash.com/random');
                setBackgroundImage(response.config.url);
            } catch (error) {
                console.error('Error fetching background image:', error);
                setBackgroundImage('/fallback-background-image.jpg'); // Set a fallback image
            }
        };

        fetchBackgroundImage();
    }, []);

    const handleInputChange = (event) => {
        setFormState({
            ...formState,
            [event.target.name]: event.target.value,
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const { username, email, password } = formState;

        try {
            const response = await register(username, email, password);
            console.log('Registration successful:', response);

            // Adjust based on the actual response structure
            if (response && response.username) {
                console.log('Registration successful:', response);
                router.push('/login'); // Redirect to login page after successful registration
            } else {
                throw new Error('Unexpected response structure');
            }
        } catch (error) {
            console.error('Registration failed:', error);
            setAlert(error.message || 'An error occurred. Please try again.');
        }
    };

    return (
        <div className={styles.authContainer} style={{ backgroundImage: `url(${backgroundImage})` }}>
            {alert && <div className={styles.alert}>{alert}</div>}
            <div className={styles.formContainer}>
                <h1 className={styles.header}>Create an account</h1>
                <p className={styles.switchModeText}>
                    Already have an account? <a href="/login" className={styles.switchModeLink}>Sign in</a>
                </p>
                <form onSubmit={handleSubmit} className={styles.form}>
                    <div className={styles.inputContainer}>
                        <label htmlFor="username" className={styles.label}>Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            className={styles.input}
                            value={formState.username}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className={styles.inputContainer}>
                        <label htmlFor="email" className={styles.label}>Email address</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            className={styles.input}
                            value={formState.email}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className={styles.inputContainer}>
                        <label htmlFor="password" className={styles.label}>Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            className={styles.input}
                            value={formState.password}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <button type="submit" className={styles.button}>Register</button>
                </form>
            </div>
        </div>
    );
};

export default SignupPage;
