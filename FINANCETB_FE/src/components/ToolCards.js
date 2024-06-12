"use client";
import React from "react";
import { Card, CardHeader, CardFooter, Image, Button } from "@nextui-org/react";
import { useRouter } from 'next/navigation'; 

export default function App({ showFooter }) {
    const router = useRouter(); // Instantiate the router

    // Function to handle navigation with the tool type as a query parameter
    const navigateToCalculator = (toolType) => {
        router.push(`/calculations?tool=${toolType}`);
    };

    const tools = [
        { type: 'Mortgage', description: 'Calculate your mortgage', imgUrl: 'https://images.unsplash.com/photo-1494526585095-c41746248156?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' },
        { type: 'Investment', description: 'Explore investment options', imgUrl: 'https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?q=80&w=870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' },
        { type: 'Loan', description: 'Manage your loans', imgUrl: 'https://plus.unsplash.com/premium_photo-1677022383099-555c0bcc63e5?q=80&w=963&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' },
        { type: 'Retirement', description: 'Plan your retirement', imgUrl: 'https://images.unsplash.com/photo-1634415486405-f67429b0ddbc?q=80&w=820&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' },
        { type: 'Emergency Fund', description: 'Build an emergency fund', imgUrl: 'https://plus.unsplash.com/premium_photo-1681469490209-c2f9f8f5c0a2?q=80&w=883&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' }
    ];

    return (
        <div className="max-w-[900px] grid grid-cols-3 gap-4 px-8 mx-auto my-12">
            {tools.slice(0, 3).map((tool, index) => (
                <Card key={index} className="h-[300px]">
                    <CardHeader className="absolute z-10 top-1 flex-col !items-start">
                        <p className="text-tiny text-white/60 uppercase font-bold">{tool.type}</p>
                        <h4 className="text-white font-medium text-large">{tool.description}</h4>
                    </CardHeader>
                    <Image
                        removeWrapper
                        alt={`${tool.type} background`}
                        className="z-0 w-full h-full object-cover"
                        src={tool.imgUrl}
                    />
                    {showFooter && (
                        <CardFooter className="absolute bg-white/30 bottom-0 border-t-1 border-zinc-100/50 z-10 justify-between">
                            <div>
                                <p className="text-black text-tiny">Available Now.</p>
                                <p className="text-black text-tiny">Get notified.</p>
                            </div>
                            <Button onClick={() => navigateToCalculator(tool.type.toLowerCase())} className="text-tiny" color="primary" radius="full" size="sm">
                                Use App
                            </Button>
                        </CardFooter>
                    )}
                </Card>
            ))}
            <div className="col-span-3 grid grid-cols-2 gap-4">
                {tools.slice(3).map((tool, index) => (
                    <Card key={index} className="h-[300px]">
                        <CardHeader className="absolute z-10 top-1 flex-col !items-start">
                            <p className="text-tiny text-white/60 uppercase font-bold">{tool.type}</p>
                            <h4 className="text-white font-medium text-large">{tool.description}</h4>
                        </CardHeader>
                        <Image
                            removeWrapper
                            alt={`${tool.type} background`}
                            className="z-0 w-full h-full object-cover"
                            src={tool.imgUrl}
                        />
                        {showFooter && (
                            <CardFooter className="absolute bg-white/30 bottom-0 border-t-1 border-zinc-100/50 z-10 justify-between">
                                <div>
                                    <p className="text-black text-tiny">Available Now.</p>
                                    <p className="text-black text-tiny">Get notified.</p>
                                </div>
                                <Button onClick={() => navigateToCalculator(tool.type.toLowerCase())} className="text-tiny" color="primary" radius="full" size="sm">
                                    Use App
                                </Button>
                            </CardFooter>
                        )}
                    </Card>
                ))}
            </div>
        </div>
    );
}
