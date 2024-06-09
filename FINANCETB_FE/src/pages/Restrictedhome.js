import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useAuth } from '../context/AuthContext';  // Adjust path as necessary
import React from 'react';
import Header from '../components/Header';
import ToolCards from '../components/ToolCards';
import ProtectedRoute from '../components/ProtectedRoute';
import NavbarComponent from '../components/NavbarComponent';



export default function RestrictedHome() {
  const { user } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!user) {
      router.push('/login');  // Redirect to login page if not logged in
    }
  }, [user, router]);

  return (
    <ProtectedRoute >
      <NavbarComponent showSignOut={true} />
    <Header isRestricted={true}/>
    <ToolCards showFooter={true} />
  </ProtectedRoute>
  );
}
