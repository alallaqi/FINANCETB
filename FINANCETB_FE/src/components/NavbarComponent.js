import React from "react";
import { Navbar, NavbarBrand, NavbarContent, NavbarItem, Link, Button, DropdownItem, DropdownTrigger, Dropdown, DropdownMenu } from "@nextui-org/react";
import { ChevronDown, Lock, Activity, Flash, Server, TagUser, Scale } from "../Assets/Icons";
import { AcmeLogo } from "../Assets/AcmeLogo.jsx";
import { useAuth } from '../context/AuthContext';
import { useRouter } from 'next/router';

export default function App() {
    const { user, logout } = useAuth();
    const router = useRouter();
    const icons = {
        chevron: <ChevronDown fill="currentColor" size={16} />,
        scale: <Scale className="text-warning" fill="currentColor" size={30} />,
        lock: <Lock className="text-success" fill="currentColor" size={30} />,
        activity: <Activity className="text-secondary" fill="currentColor" size={30} />,
        flash: <Flash className="text-primary" fill="currentColor" size={30} />,
        server: <Server className="text-success" fill="currentColor" size={30} />,
        user: <TagUser className="text-danger" fill="currentColor" size={30} />,
    };

    const handleLogout = () => {
        logout();
        router.push('/home'); // Redirects to the home page after logout
    };

    return (
        <Navbar className="navbar">
            <NavbarBrand onClick={() => router.push(user ? "/Restrictedhome" : "/home")} className="navbar-brand">
                <AcmeLogo />
                <p className="font-bold text-inherit cursor-pointer">FINANCETB</p>
            </NavbarBrand>
            <NavbarContent className="navbar-content" justify="center">
                <Dropdown>
                    <NavbarItem>
                        <DropdownTrigger>
                            <Button
                                disableRipple
                                className="p-0 bg-transparent data-[hover=true]:bg-transparent"
                                endContent={icons.chevron}
                                radius="sm"
                                variant="light"
                            >
                                Tools
                            </Button>
                        </DropdownTrigger>
                    </NavbarItem>
                    <DropdownMenu
                        aria-label="ACME Tools"
                        className="w-[340px]"
                    >
                        <DropdownItem
                            key="mortgage_calculator"
                            description="Calculate your monthly mortgage payments quickly and easily."
                            startContent={icons.scale}
                        >
                            <Link href="/calculations?tool=mortgage">
                                Mortgage Calculator
                            </Link>
                        </DropdownItem>
                        <DropdownItem
                            key="loan_calculator"
                            description="Estimate your loan repayments with our simple tool."
                            startContent={icons.lock}
                        >
                            <Link href="/calculations?tool=loan">
                                Loan Calculator
                            </Link>
                        </DropdownItem>
                        <DropdownItem
                            key="investment_calculator"
                            description="Explore potential returns on your investments."
                            startContent={icons.flash}
                        >
                            <Link href="/calculations?tool=investment">
                                Investment Calculator
                            </Link>
                        </DropdownItem>
                        <DropdownItem
                            key="retirement_calculator"
                            description="Plan for your retirement with our detailed projection tool."
                            startContent={icons.server}
                        >
                            <Link href="/calculations?tool=retirement">
                                Retirement Calculator
                            </Link>
                        </DropdownItem>
                        <DropdownItem
                            key="emergency_fund_calculator"
                            description="Calculate how much you need to save for emergencies."
                            startContent={icons.user}
                        >
                            <Link href="/calculations?tool=emergencyfund">
                                Emergency Fund Calculator
                            </Link>
                        </DropdownItem>
                    </DropdownMenu>
                </Dropdown>
                <NavbarItem isActive>
                    <Link href={user ? "/Restrictedhome" : "/home"} aria-current="page">
                        Home
                    </Link>
                </NavbarItem>
            </NavbarContent>
            <NavbarContent justify="end">
                {user ? (
                    <NavbarItem>
                        <Button onClick={handleLogout} as={Link} color="primary" href="/home" variant="flat">
                            Sign Out
                        </Button>
                    </NavbarItem>
                ) : (
                    <>
                        <NavbarItem className="hidden lg:flex">
                            <Link href="/login">Login</Link>
                        </NavbarItem>
                        <NavbarItem>
                            <Button as={Link} color="primary" href="/register" variant="flat">
                                Sign Up
                            </Button>
                        </NavbarItem>
                    </>
                )}
            </NavbarContent>
        </Navbar>
    );
}
