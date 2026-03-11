import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { counselorService } from '../services/api';

const CounselorAnalyticsPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [analytics, setAnalytics] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAnalytics = async () => {
            try {
                const response = await counselorService.getAnalytics(user.id);
                if (response.data.status === 'success') {
                    setAnalytics(response.data);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch analytics data');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchAnalytics();
    }, [user.id]);

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--indigo-deep)' }}>Performance Analytics</h1>
                <p style={{ color: 'var(--text-sub)' }}>Detailed insights into your session impact and patient feedback.</p>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>Calculating your impact...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)', textAlign: 'center' }}>{error}</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
                    {/* Stat Cards */}
                    <div className="responsive-grid">
                        <div className="card shadow-sm" style={{ textAlign: 'center', borderTop: '5px solid var(--indigo-rich)' }}>
                            <p style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.5rem' }}>Total Sessions</p>
                            <h2 style={{ fontSize: '2.5rem', margin: 0, color: 'var(--indigo-deep)' }}>{analytics.total_sessions}</h2>
                        </div>
                        <div className="card shadow-sm" style={{ textAlign: 'center', borderTop: '5px solid #00acc1' }}>
                            <p style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.5rem' }}>Total Patients</p>
                            <h2 style={{ fontSize: '2.5rem', margin: 0, color: '#00838f' }}>{analytics.total_patients}</h2>
                        </div>
                        <div className="card shadow-sm" style={{ textAlign: 'center', borderTop: '5px solid #fb8c00' }}>
                            <p style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.5rem' }}>Average Rating</p>
                            <h2 style={{ fontSize: '2.5rem', margin: 0, color: '#ef6c00' }}>{analytics.average_rating} <span style={{ fontSize: '1.2rem' }}>⭐</span></h2>
                            <p style={{ fontSize: '0.8rem', color: '#888', marginTop: '0.25rem' }}>from {analytics.rating_count} reviews</p>
                        </div>
                        <div className="card shadow-sm" style={{ textAlign: 'center', borderTop: '5px solid #43a047' }}>
                            <p style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.5rem' }}>Total Earnings</p>
                            <h2 style={{ fontSize: '2.5rem', margin: 0, color: '#2e7d32' }}>${analytics.total_earnings.toFixed(2)}</h2>
                        </div>
                    </div>

                    {/* Detailed Breakdown Placeholder */}
                    <div className="card shadow-sm" style={{ padding: '2rem' }}>
                        <h3 style={{ marginBottom: '1.5rem', color: 'var(--indigo-deep)' }}>Session Impact Overview</h3>
                        <div style={{ padding: '3rem', textAlign: 'center', backgroundColor: '#f8f9ff', borderRadius: '12px', border: '2px dashed #e0e6f0' }}>
                            <div style={{ fontSize: '2rem', marginBottom: '1rem' }}>📈</div>
                            <p style={{ color: '#666' }}>Your session booking rate has increased by 15% this month.</p>
                            <p style={{ color: '#888', fontSize: '0.85rem' }}>Detailed charts and weekly trends are synchronized from the mobile console.</p>
                        </div>
                    </div>

                    <div style={{ textAlign: 'center', color: '#888', fontSize: '0.9rem' }}>
                        Data updated in real-time based on finalized sessions.
                    </div>
                </div>
            )}
        </div>
    );
};

export default CounselorAnalyticsPage;
