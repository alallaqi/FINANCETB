"use client";
import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../config/axiosConfig';
import styles from '../styles/Header.module.css';
import { Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from "@nextui-org/react";

function Header({ isRestricted }) {
    const [selectedColor, setSelectedColor] = useState("default");
    const [calculations, setCalculations] = useState([]);
    const { user, logout } = useAuth();

    useEffect(() => {
        if (user && user.id) {
            const fetchUserCalculations = async () => {
                try {
                    const response = await axiosInstance.get(`/api/users/profile/${user.id}`);
                    console.log("User in Header:", response.data);
        
                    const calculations = response.data.calculations || [];
                    console.log("Calculations fetched:", calculations);
        
                    const latestCalculationsMap = calculations.reduce((acc, calc) => {
                        if (!acc[calc.type] || new Date(acc[calc.type].timestamp) < new Date(calc.timestamp)) {
                            acc[calc.type] = calc;
                        }
                        return acc;
                    }, {});
        
                    const latestCalculations = Object.values(latestCalculationsMap);
                    console.log("Latest calculations:", latestCalculations);
        
                    setCalculations(latestCalculations);
                } catch (error) {
                    console.error('Failed to fetch calculations:', error);
                    if (error.response && error.response.status === 401) {
                        logout();
                    }
                }
            };
            fetchUserCalculations();
        }
    }, [user, logout]);
    

    return (
        <div className={styles.headerContainer}>
            <img
                src="https://plus.unsplash.com/premium_photo-1681469490069-753438d8c3a1?q=80&w=1064&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                alt="Header Background"
                className={styles.backgroundImage}
            />
            <div className={styles.overlayBKG}></div>
            <div className={styles.content}>
                <h1 className={styles.title}>Financial Toolbox</h1>
                <p className={styles.subtitle}>
                    {isRestricted && user ? `Welcome, ${user.username}` : "Your personal financial coach in your pocket."}
                </p>
            </div>
            {isRestricted && calculations.length > 0 && (
    <div className="flex flex-col gap-3">
        <Table color={selectedColor} selectionMode="single">
            <TableHeader>
                <TableColumn>CALCULATION</TableColumn>
                <TableColumn>RESULT</TableColumn>
                <TableColumn>TIME</TableColumn>
            </TableHeader>
            <TableBody>
                {calculations.map((calc, index) => (
                    <TableRow key={index}>
                        <TableCell>{calc.type}</TableCell>
                        <TableCell>{calc.result}</TableCell>
                        <TableCell>{new Date(calc.timestamp).toLocaleString()}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    </div>
)}
        </div>
    );
}

export default Header;
