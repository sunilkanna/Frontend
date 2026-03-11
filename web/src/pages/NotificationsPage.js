import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { notificationService } from '../services/api';

const NotificationsPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                const response = await notificationService.getNotifications(user.id);
                if (response.data.status === 'success') {
                    setNotifications(response.data.notifications);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch notifications');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchNotifications();
    }, [user.id]);

    const handleMarkAsRead = async (id) => {
        try {
            const response = await notificationService.markAsRead(id);
            if (response.data.status === 'success') {
                setNotifications(notifications.map(n => n.id === id ? { ...n, is_read: 1 } : n));
            }
        } catch (err) {
            console.error('Failed to mark notification as read', err);
        }
    };

    const getIcon = (type) => {
        switch (type) {
            case 'Appointment': return '📅';
            case 'Session': return '🎥';
            case 'Report': return '📄';
            default: return '🔔';
        }
    };

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: user.user_type === 'Counselor' ? 'var(--indigo-deep)' : 'var(--teal-dark)' }}>Notifications</h1>
                <p style={{ color: 'var(--text-sub)' }}>Stay updated with your latest alerts and activities.</p>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>Loading alerts...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)', textAlign: 'center' }}>{error}</div>
            ) : notifications.length === 0 ? (
                <div className="card" style={{ textAlign: 'center', padding: '4rem' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📭</div>
                    <h3>All caught up!</h3>
                    <p style={{ color: '#666' }}>No new notifications yet. We'll alert you when something important happens.</p>
                </div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    {notifications.map((n) => (
                        <div
                            key={n.id}
                            className={`card shadow-sm ${!n.is_read ? 'unread-notification' : ''}`}
                            style={{
                                display: 'flex',
                                gap: '1rem',
                                padding: '1.25rem',
                                borderLeft: !n.is_read ? `5px solid ${user.user_type === 'Counselor' ? 'var(--indigo-rich)' : 'var(--teal-main)'}` : '5px solid transparent',
                                backgroundColor: !n.is_read ? '#f8f9ff' : 'white',
                                cursor: 'pointer'
                            }}
                            onClick={() => !n.is_read && handleMarkAsRead(n.id)}
                        >
                            <div style={{ fontSize: '1.5rem' }}>{getIcon(n.type)}</div>
                            <div style={{ flex: 1 }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                                    <h4 style={{ margin: 0, fontWeight: !n.is_read ? 'bold' : 'normal' }}>{n.title}</h4>
                                    <span style={{ fontSize: '0.75rem', color: '#888' }}>{new Date(n.created_at).toLocaleString()}</span>
                                </div>
                                <p style={{ margin: 0, fontSize: '0.9rem', color: '#555' }}>{n.message}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default NotificationsPage;
