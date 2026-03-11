import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { dashboardService } from '../services/api';

const AdminDashboardPage = () => {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAdminStats = async () => {
            try {
                const response = await dashboardService.getAdminStats();
                if (response.data.status === 'success') {
                    setStats(response.data);
                }
            } catch (err) {
                console.error('Failed to fetch admin stats', err);
            } finally {
                setLoading(false);
            }
        };

        fetchAdminStats();
    }, []);

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--indigo-deep)' }}>Admin Control Panel</h1>
                <p style={{ color: 'var(--text-sub)' }}>Platform-wide overview and system management.</p>
            </div>

            {loading ? (
                <div>Loading stats...</div>
            ) : (
                <>
                    <div className="responsive-grid">
                        <div className="card" style={{ background: 'linear-gradient(135deg, #6a1b9a 0%, #8e24aa 100%)', color: 'white' }}>
                            <h4 style={{ color: 'rgba(255,255,255,0.8)' }}>Pending Verifications</h4>
                            <h2>{stats?.pending_verifications}</h2>
                        </div>
                        <div className="card" style={{ background: 'linear-gradient(135deg, #6a1b9a 0%, #8e24aa 100%)', color: 'white' }}>
                            <h4 style={{ color: 'rgba(255,255,255,0.8)' }}>Active Counselors</h4>
                            <h2>{stats?.active_counselors}</h2>
                        </div>
                        <div className="card" style={{ background: 'linear-gradient(135deg, #6a1b9a 0%, #8e24aa 100%)', color: 'white' }}>
                            <h4 style={{ color: 'rgba(255,255,255,0.8)' }}>Total Patients</h4>
                            <h2>{stats?.total_patients}</h2>
                        </div>
                        <div className="card" style={{ background: 'linear-gradient(135deg, #6a1b9a 0%, #8e24aa 100%)', color: 'white' }}>
                            <h4 style={{ color: 'rgba(255,255,255,0.8)' }}>System Alerts</h4>
                            <h2>{stats?.system_alerts}</h2>
                        </div>
                    </div>

                    <div style={{ marginTop: '3rem' }}>
                        <h3 style={{ marginBottom: '1.5rem' }}>Quick Actions</h3>
                        <div className="responsive-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))' }}>
                            <div className="card action-card" onClick={() => navigate('/verify')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>✓</div>
                                <strong>Verify Counselors</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>{stats?.pending_verifications} pending</p>
                            </div>
                            <div className="card action-card" onClick={() => navigate('/analytics')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>📊</div>
                                <strong>Analytics</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>Platform metrics</p>
                            </div>
                            <div className="card action-card" onClick={() => navigate('/users')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>👥</div>
                                <strong>User Management</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>Manage users</p>
                            </div>
                            <div className="card action-card" onClick={() => navigate('/reports')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>📋</div>
                                <strong>Reports & Logs</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>System reports</p>
                            </div>
                            <div className="card action-card" onClick={() => navigate('/notifications')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>🔔</div>
                                <strong>Notifications</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>Send announcements</p>
                            </div>
                            <div className="card action-card" onClick={() => navigate('/settings')} style={{ cursor: 'pointer', transition: 'transform 0.2s' }}>
                                <div style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: 'var(--primary)' }}>⚙️</div>
                                <strong>System Settings</strong>
                                <p style={{ fontSize: '0.85rem', color: 'var(--text-sub)', margin: '0.5rem 0 0' }}>Manage platform</p>
                            </div>
                        </div>
                    </div>

                    <div className="responsive-grid" style={{ marginTop: '3rem', alignItems: 'start' }}>
                        <div className="card">
                            <h3>Recent Activity</h3>
                            <div style={{ marginTop: '1rem' }}>
                                <div style={{ padding: '0.75rem 0', borderBottom: '1px solid #f4f5f7' }}>
                                    <strong>Dr. Sarah Jefferson verified</strong>
                                    <p style={{ fontSize: '0.8rem', color: 'var(--text-sub)', margin: 0 }}>10 mins ago</p>
                                </div>
                                <div style={{ padding: '0.75rem 0', borderBottom: '1px solid #f4f5f7' }}>
                                    <strong style={{ color: 'var(--error)' }}>Server usage at 85%</strong>
                                    <p style={{ fontSize: '0.8rem', color: 'var(--text-sub)', margin: 0 }}>1 hour ago</p>
                                </div>
                                <div style={{ padding: '0.75rem 0', borderBottom: '1px solid #f4f5f7' }}>
                                    <strong>25 new patient registrations</strong>
                                    <p style={{ fontSize: '0.8rem', color: 'var(--text-sub)', margin: 0 }}>2 hours ago</p>
                                </div>
                            </div>
                        </div>

                        <div className="card">
                            <h3>Platform Health</h3>
                            <div style={{ marginTop: '1rem' }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem 0', borderBottom: '1px solid #f4f5f7' }}>
                                    <span>Server Status</span>
                                    <strong style={{ color: 'var(--success)' }}>Online</strong>
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem 0', borderBottom: '1px solid #f4f5f7' }}>
                                    <span>Database</span>
                                    <strong style={{ color: stats?.system_alerts > 0 ? 'var(--error)' : 'var(--success)' }}>
                                        {stats?.system_alerts > 0 ? 'Needs Attention' : 'Healthy'}
                                    </strong>
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem 0' }}>
                                    <span>API Response</span>
                                    <strong style={{ color: 'var(--success)' }}>45ms</strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default AdminDashboardPage;
