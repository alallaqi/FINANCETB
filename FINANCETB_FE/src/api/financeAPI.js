import axios from 'axios';

const API_BASE_URL = 'https://boiling-lowlands-43453-ff95b478022d.herokuapp.com/api'; // Adjust this as necessary to your backend API's base URL

const getAuthHeaders = (token) => ({
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
});

export const calculateMortgage = async (mortgageData, token) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/mortgage/calculate`, mortgageData, getAuthHeaders(token));
        return response.data;
    } catch (error) {
        console.error('Failed to calculate mortgage:', error);
        throw error; // Rethrow the error for handling it in the component
    }
};

export const calculateLoan = async (loanData, token) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/loan/calculate`, loanData, getAuthHeaders(token));
        return response.data;
    } catch (error) {
        console.error('Failed to calculate loan:', error);
        throw error;
    }
};

export const calculateInvestment = async (investmentData, token) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/investment/calculate`, investmentData, getAuthHeaders(token));
        return response.data;
    } catch (error) {
        console.error('Failed to calculate investment:', error);
        throw error;
    }
};

export const calculateEmergencyFund = async (emergencyFundData, token) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/emergencyfund/calculate`, emergencyFundData, getAuthHeaders(token));
        return response.data;
    } catch (error) {
        console.error('Failed to calculate emergency fund:', error);
        throw error;
    }
};

export const calculateRetirementPlan = async (retirementPlanData, token) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/retirementplan/calculate`, retirementPlanData, getAuthHeaders(token));
        return response.data;
    } catch (error) {
        console.error('Failed to calculate retirement plan:', error);
        throw error;
    }
};