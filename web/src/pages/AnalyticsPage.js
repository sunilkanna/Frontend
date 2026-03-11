import React, { useEffect, useState } from 'react';
import { adminService } from '../services/api';

const AnalyticsPage = () => {
    const [analytics, setAnalytics] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchAnalytics = async () => {
            try {
                const response = await adminService.getAnalytics();
                if (response.data.status === 'success') {
                    setAnalytics(response.data.analytics);
                }
            } catch (err) {
                console.error('Failed to load analytics');
            } finally {
                setLoading(false);
            }
        };
        fetchAnalytics();
    }, []);

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--indigo-deep)' }}>Platform Analytics</h1>
                <p style={{ color: 'var(--text-sub)' }}>Deep insights into platform growth, usage, and revenue metrics.</p>
            </div>

            {loading ? (
                <div>Loading analytics...</div>
            ) : (
                <div className="responsive-grid">
                    <div className="card">
                        <h4>Monthly Revenue</h4>
                        <h2 style={{ color: 'var(--success)' }}>${analytics?.monthly_revenue}</h2>
                        <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', marginTop: '0.5rem' }}>Growth: +12% this month</p>
                    </div>
                    <div className="card">
                        <h4>New Users</h4>
                        <h2>{analytics?.new_users_count}</h2>
                        <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', marginTop: '0.5rem' }}>Last 30 days</p>
                    </div>
                    <div className="card">
                        <h4>Completed Sessions</h4>
                        <h2>{analytics?.completed_sessions}</h2>
                        <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', marginTop: '0.5rem' }}>78% success rate</p>
                    </div>

                    <div className="card" style={{ gridColumn: '1 / -1' }}>
                        <h3>Top Performing Counselors</h3>
                        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '1rem' }}>
                            <thead>
                                <tr style={{ textAlign: 'left', borderBottom: '1px solid #dfe1e6' }}>
                                    <th style={{ padding: '0.75rem' }}>Counselor</th>
                                    <th style={{ padding: '0.75rem' }}>Sessions</th>
                                    <th style={{ padding: '0.75rem' }}>Rating</th>
                                    <th style={{ padding: '0.75rem' }}>Revenue</th>
                                </tr>
                            </thead>
                            <tbody>
                                {analytics?.top_counselors?.map((c, i) => (
                                    <tr key={i} style={{ borderBottom: '1px solid #f4f5f7' }}>
                                        <td style={{ padding: '0.75rem' }}>{c.name}</td>
                                        <td style={{ padding: '0.75rem' }}>{c.sessions}</td>
                                        <td style={{ padding: '0.75rem' }}>{c.rating} ⭐</td>
                                        <td style={{ padding: '0.75rem' }}>${c.revenue}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AnalyticsPage;
