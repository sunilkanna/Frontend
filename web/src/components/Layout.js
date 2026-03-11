import React from 'react';
import Sidebar from './Sidebar';
import TopNavbar from './TopNavbar';

const Layout = ({ children }) => {
    return (
        <div className="app-layout">
            <Sidebar />
            <div className="main-content">
                <TopNavbar />
                <main className="page-container">
                    {children}
                </main>
            </div>
        </div>
    );
};

export default Layout;
