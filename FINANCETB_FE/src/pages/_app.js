// Import the necessary modules from NextUI and React
import { NextUIProvider } from '@nextui-org/react';
import { AuthProvider } from '../context/AuthContext'; // Adjust path as necessary
import '../app/globals.css';

function MyApp({ Component, pageProps }) {


  return (
    <AuthProvider>
      <NextUIProvider>
        <Component {...pageProps} />
      </NextUIProvider>
    </AuthProvider>
  );
}

export default MyApp;
