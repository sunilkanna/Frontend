import React, { useEffect, useState } from 'react';
import { adminService } from '../services/api';

const UserManagementPage = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await adminService.getUsers();
                if (response.data.status === 'success') {
                    setUsers(response.data.users);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch users');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    const handleAction = async (userId, action) => {
        if (!window.confirm(`Are you sure you want to ${action} this user?`)) return;
        try {
            const response = await adminService.manageUser({ user_id: userId, action });
            if (response.data.status === 'success') {
                alert('Action success');
                setUsers(prev => prev.filter(u => u.id !== userId || action !== 'delete'));
            } else {
                alert(response.data.message);
            }
        } catch (err) {
            alert('Failed to perform action');
        }
    };

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--indigo-deep)' }}>User Management</h1>
                <p style={{ color: 'var(--text-sub)' }}>Manage all registered Patients and Counselors across the system.</p>
            </div>

            {loading ? (
                <div>Loading users...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)' }}>{error}</div>
            ) : (
                <div className="card" style={{ padding: 0, overflowX: 'auto' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                        <thead>
                            <tr style={{ background: '#f4f5f7', textAlign: 'left' }}>
                                <th style={{ padding: '1rem' }}>Name</th>
                                <th style={{ padding: '1rem' }}>Email</th>
                                <th style={{ padding: '1rem' }}>Type</th>
                                <th style={{ padding: '1rem' }}>Joined</th>
                                <th style={{ padding: '1rem' }}>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users.map(user => (
                                <tr key={user.id} style={{ borderBottom: '1px solid #f4f5f7' }}>
                                    <td style={{ padding: '1rem' }}>{user.full_name}</td>
                                    <td style={{ padding: '1rem' }}>{user.email}</td>
                                    <td style={{ padding: '1rem' }}>
                                        <span style={{
                                            padding: '0.25rem 0.5rem',
                                            borderRadius: '4px',
                                            fontSize: '0.8rem',
                                            backgroundColor: user.user_type === 'Patient' ? '#e1f5fe' : '#e8f5e9',
                                            color: user.user_type === 'Patient' ? '#0288d1' : '#2e7d32'
                                        }}>
                                            {user.user_type}
                                        </span>
                                    </td>
                                    <td style={{ padding: '1rem' }}>{new Date(user.created_at).toLocaleDateString()}</td>
                                    <td style={{ padding: '1rem' }}>
                                        <button
                                            onClick={() => handleAction(user.id, 'delete')}
                                            style={{ color: 'var(--error)', background: 'none', border: 'none', fontSize: '0.9rem' }}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default UserManagementPage;
