import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../context/AuthContext';
import React from "react";


function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!loading && !isAuthenticated) {
      router.push('/home');  // Redirect to '/auth' if not authenticated
    }
  }, [isAuthenticated, loading, router]);

  if (loading) {
    return <div>Loading...</div>;  // Or any loading component
  }

  return isAuthenticated ? children : null;
}

export default ProtectedRoute;
