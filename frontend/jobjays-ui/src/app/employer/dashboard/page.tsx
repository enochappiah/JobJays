"use client";

import React from "react";
import styles from "@/styles/dashboard.module.css";
import { useEmployer, useImpressionChartData } from "@/lib/api";

import { Bar, BarChart, CartesianGrid, XAxis } from "recharts";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";

import {
    ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart";

const chartConfig = {
    impressions: {
        label: "Impressions",
        color: "hsl(var(--chart-1))",
    },
} satisfies ChartConfig;

const DashboardPage: React.FC = () => {
    const { EmployerProfile, isLoading, isError } = useEmployer();
    const { impressionData, isLoading: chartLoading, isError: chartError } =
        useImpressionChartData();
    const [activeChart, setActiveChart] =
        React.useState<keyof typeof chartConfig>("impressions");

    // Calculate the total impressions using useMemo
    const total = React.useMemo(() => {
        if (!impressionData || impressionData.length === 0) {
            return { impressions: 0 };
        }
        return {
            impressions: impressionData.reduce(
                (acc, curr) => acc + (curr.impressions || 0),
                0
            ),
        };
    }, [impressionData]);

    if (chartLoading) return <div>Loading...</div>;
    if (chartError) return <div>Error loading chart data.</div>;
    if (isLoading) return <div>Loading...</div>;
    if (isError) return <div>Error loading employer profile.</div>;

    return (
        <div>
            <header className={styles.dashboardHeader}>
                <h3>Hello, {EmployerProfile.name}</h3>
                <div className={styles.statsOverview}>
                    <div className={styles.statBox}>
                        <p>{EmployerProfile.jobPostsSize}</p>
                        <span>Jobs</span>
                    </div>
                    <div className={styles.statBox}>
                        <p>
                            {EmployerProfile.jobPosts.reduce(
                                (totalApplications, jobPost) =>
                                    totalApplications + (jobPost.numApplicants || 0),
                                0
                            )}
                        </p>
                        <span>Applicants</span>
                    </div>
                </div>
            </header>
            <div className={"py-5"}>
                <Card>
                    <CardHeader className="flex flex-col items-stretch space-y-0 border-b p-0 sm:flex-row">
                        <div className="flex flex-1 flex-col justify-center gap-1 px-6 py-5 sm:py-6">
                            <CardTitle>Bar Chart - Interactive</CardTitle>
                            <CardDescription>
                                Showing total Impressions for the last 3 months
                            </CardDescription>
                        </div>
                        <div className="flex">
                            {["impressions"].map((key) => {
                                const chart = key as keyof typeof chartConfig;
                                return (
                                    <button
                                        key={chart}
                                        data-active={activeChart === chart}
                                        className="relative z-30 flex flex-1 flex-col justify-center gap-1 border-t px-6 py-4 text-left even:border-l data-[active=true]:bg-muted/50 sm:border-l sm:border-t-0 sm:px-8 sm:py-6"
                                        onClick={() => setActiveChart(chart)}
                                    >
                    <span className="text-xs text-muted-foreground">
                      {chartConfig[chart].label}
                    </span>
                                        <span className="text-lg font-bold leading-none sm:text-3xl">
                      {total[chart].toLocaleString()}
                    </span>
                                    </button>
                                );
                            })}
                        </div>
                    </CardHeader>
                    <CardContent className="px-2 sm:p-6">
                        <ChartContainer
                            config={chartConfig}
                            className="aspect-auto h-[250px] w-full"
                        >
                            <BarChart
                                accessibilityLayer
                                data={impressionData}
                                margin={{
                                    left: 12,
                                    right: 12,
                                }}
                            >
                                <CartesianGrid vertical={false} />
                                <XAxis
                                    dataKey="date"
                                    tickLine={false}
                                    axisLine={false}
                                    tickMargin={8}
                                    minTickGap={32}
                                    tickFormatter={(value) => {
                                        const date = new Date(value);
                                        return date.toLocaleDateString("en-US", {
                                            month: "short",
                                            day: "numeric",
                                        });
                                    }}
                                />
                                <ChartTooltip
                                    content={
                                        <ChartTooltipContent
                                            className="w-[150px]"
                                            nameKey="impressions"
                                            labelFormatter={(value) => {
                                                return new Date(value).toLocaleDateString("en-US", {
                                                    month: "short",
                                                    day: "numeric",
                                                    year: "numeric",
                                                });
                                            }}
                                        />
                                    }
                                />
                                <Bar
                                    dataKey="impressions"
                                    fill={chartConfig.impressions.color}
                                />
                            </BarChart>
                        </ChartContainer>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
};

export default DashboardPage;
