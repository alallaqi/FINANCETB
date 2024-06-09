import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { MortgageCalculator, InvestmentCalculator, LoanCalculator, RetirementCalculator, EmergencyFundCalculator } from '../components/FinancialCalculators'; 
import Header from '../components/Header';
import NavbarComponent from '../components/NavbarComponent';
import ProtectedRoute from '../components/ProtectedRoute';

const CalculationsPage = () => {
  const router = useRouter();
  const { tool } = router.query;

  useEffect(() => {
    console.log(`Loaded calculator for: ${tool}`);
  }, [tool]);

  const renderCalculator = () => {
    switch(tool) {
      case 'mortgage':
        return <MortgageCalculator />;
      case 'investment':
        return <InvestmentCalculator />;
      case 'loan':
        return <LoanCalculator />;
      case 'retirement':
        return <RetirementCalculator />;
      case 'emergency':
        return <EmergencyFundCalculator />;
      default:
        return <p>Select a tool to see the calculator.</p>;
    }
  };

  return (
    <ProtectedRoute>
      <NavbarComponent />
      <Header isRestricted={true}/>
      {renderCalculator()}
    </ProtectedRoute>
  );
};

export default CalculationsPage;
