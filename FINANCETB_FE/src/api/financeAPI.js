import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Adjust this as necessary to your backend API's base URL

export const calculateMortgage = async (mortgageData) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/mortgage/calculate`, mortgageData);
        return response.data;
    } catch (error) {
        console.error('Failed to calculate mortgage:', error);
        throw error; // Rethrow the error for handling it in the component
    }
};
