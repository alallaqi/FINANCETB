import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';
import { useAuth } from '../context/AuthContext';
import styles from '../styles/Auth.module.css';

const LoginPage = () => {
  const [formState, setFormState] = useState({
    username: '',
    password: '',
  });
  const [backgroundImage, setBackgroundImage] = useState('');
  const [alert, setAlert] = useState('');
  const { user, login } = useAuth();
  const router = useRouter();

  useEffect(() => {
    // Fetch a random background image or set a fallback
    const fetchBackgroundImage = async () => {
      try {
        const response = await axios.get('https://source.unsplash.com/random');
        setBackgroundImage(response.config.url);
      } catch (error) {
        console.error('Error fetching background image:', error);
        setBackgroundImage('/fallback-background-image.jpg');
      }
    };
    fetchBackgroundImage();

    // Redirect if already logged in
    if (user && user.id) {
      router.push('/Restrictedhome');
    }
  }, [user, router]);  // Added user and router as dependencies

  const handleInputChange = (event) => {
    setFormState({
      ...formState,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const { username, password } = formState;
      await login(username, password);
      router.push('/Restrictedhome'); // Redirect after successful login
    } catch (error) {
      console.error('Login failed:', error);
      setAlert(error.response?.data?.message || 'An error occurred. Please try again.');
    }
  };

  return (
    <div className={styles.authContainer} style={{ backgroundImage: `url(${backgroundImage})` }}>
      {alert && <div className={styles.alert}>{alert}</div>}
      <div className={styles.formContainer}>
        <h1 className={styles.header}>Sign in</h1>
        <p className={styles.switchModeText}>
          New user? <a href="/register" className={styles.switchModeLink}>Create an account</a>
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
          <button type="submit" className={styles.button}>Continue</button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
