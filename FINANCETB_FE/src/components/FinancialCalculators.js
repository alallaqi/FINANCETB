import React, { useState } from 'react';
import axiosInstance from '../config/axiosConfig';
import { Input, Button, Card, CardHeader, CardBody, CardFooter, Divider, Link, Image } from '@nextui-org/react';

// Helper function to create a calculator component
const createCalculator = (title, apiEndpoint, fields, resultKey) => {
  return function Calculator() {
    const [inputs, setInputs] = useState(fields.reduce((acc, field) => ({ ...acc, [field.name]: '' }), {}));
    const [result, setResult] = useState('');

    const handleChange = (e) => {
      const { name, value } = e.target;
      setInputs(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      for (const field of fields) {
        if (!inputs[field.name] || inputs[field.name] <= 0) {
          console.error("Invalid input values.");
          return;
        }
      }

      try {
        const response = await axiosInstance.post(apiEndpoint, inputs);
        if (response.data && resultKey in response.data) {
          setResult(`${title} Result: ${response.data[resultKey]}`);
        } else {
          console.error("Unexpected response structure. Check the backend.");
          setResult("Unexpected response structure. Check the backend.");
        }
      } catch (error) {
        console.error(`Error calculating ${title.toLowerCase()}:`, error);
        setResult(`Error calculating ${title.toLowerCase()}. Please check the inputs and try again.`);
      }
    };

    return (
      <Card className="max-w-[400px] mx-auto mt-8">
        <CardHeader className="flex gap-3">
          <Image
            alt="nextui logo"
            height={40}
            radius="sm"
            src="https://avatars.githubusercontent.com/u/86160567?s=200&v=4"
            width={40}
          />
          <div className="flex flex-col">
            <p className="text-md">{title} Calculator</p>
          </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            {fields.map(field => (
              <Input
                key={field.name}
                clearable
                bordered
                fullWidth
                label={field.label}
                placeholder={field.placeholder}
                value={inputs[field.name]}
                onChange={handleChange}
                name={field.name}
              />
            ))}
            <Button color="primary" variant="flat" type="submit">Calculate</Button>
            {result && <div style={{ textAlign: 'center', marginTop: '20px' }}>{result}</div>}
          </form>
        </CardBody>
        <Divider />
        <CardFooter>
          <Link isExternal showAnchorIcon href="https://github.com/nextui-org/nextui">
            Calculate Your Next Item
          </Link>
        </CardFooter>
      </Card>
    );
  };
};

const MortgageCalculator = createCalculator("Mortgage", "/api/mortgage/calculate", [
  { name: "principal", label: "Principal", placeholder: "Enter principal amount" },
  { name: "interestRate", label: "Interest Rate (%)", placeholder: "Enter interest rate" },
  { name: "term", label: "Term (years)", placeholder: "Enter term in years" }
], "mortgageResult");

const InvestmentCalculator = createCalculator("Investment", "http://localhost:8080/api/investment/calculate", [
  { name: "amount", label: "Investment Amount", placeholder: "Enter investment amount" },
  { name: "rate", label: "Annual Interest Rate (%)", placeholder: "Enter annual interest rate" },
  { name: "years", label: "Investment Period (years)", placeholder: "Enter number of years" }
], "investmentResult");

const LoanCalculator = createCalculator("Loan", "/api/loan/calculate", [
  { name: "amount", label: "Loan Amount", placeholder: "Enter loan amount" },
  { name: "interest", label: "Interest Rate (%)", placeholder: "Enter interest rate" },
  { name: "term", label: "Loan Term (years)", placeholder: "Enter term in years" }
], "loanResult");

const RetirementCalculator = createCalculator("Retirement", "/api/retirement/calculate", [
  { name: "currentAge", label: "Current Age", placeholder: "Enter your current age" },
  { name: "retirementAge", label: "Retirement Age", placeholder: "Enter your retirement age" },
  { name: "monthlyContribution", label: "Monthly Contribution", placeholder: "Enter your monthly contribution" },
  { name: "currentSavings", label: "Current Savings", placeholder: "Enter your current savings" },
  { name: "annualReturn", label: "Annual Return (%)", placeholder: "Enter the annual return rate" }
], "retirementResult");


const EmergencyFundCalculator = createCalculator("Emergency Fund", "/api/emergencyfund/calculate", [
  { name: "monthlyExpenses", label: "Monthly Expenses", placeholder: "Enter monthly expenses" },
  { name: "targetMonths", label: "Target Fund Duration (months)", placeholder: "Enter target duration in months" }
], "emergencyFund");

export { MortgageCalculator, InvestmentCalculator, LoanCalculator, RetirementCalculator, EmergencyFundCalculator };
