'use client';
import React from 'react';
import EmployerSidebar from "@/components/employer/EmployerSidebar";


const EmployerLayout = ({ children }: { children: React.ReactNode }) => {

    return (
        <div className="flex h-screen bg-gray-50">
            {/* Sidebar */}
            <EmployerSidebar/>
            {/* Main Content */}
            <main className="flex-1 overflow-y-auto">
                <div className="container mx-auto px-6 py-8">
                    {children}
                </div>
            </main>
        </div>
    );
};

export default EmployerLayout;