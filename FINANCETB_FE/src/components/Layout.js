import React from 'react';
import NavbarComponent from '../components/NavbarComponent'; // The file where your Navbar code is saved

const Layout = ({ children }) => {
  return (
    <>
      <NavbarComponent />
      <main>{children}</main>
    </>
  );
};

export default Layout;
