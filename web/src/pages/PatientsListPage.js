import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { counselorService } from '../services/api';

const PatientsListPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchPatients = async () => {
            try {
                const response = await counselorService.getPatients(user.id);
                if (response.data.status === 'success') {
                    setPatients(response.data.patients);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch patient list');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchPatients();
    }, [user.id]);

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--indigo-deep)' }}>My Patients</h1>
                <p style={{ color: 'var(--text-sub)' }}>Monitor and manage the health journeys of your assigned patients.</p>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>Loading patient list...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)', textAlign: 'center' }}>{error}</div>
            ) : patients.length === 0 ? (
                <div className="card" style={{ textAlign: 'center', padding: '4rem' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>👥</div>
                    <h3>No Patients Found</h3>
                    <p style={{ color: '#666' }}>You haven't been assigned any patients yet.</p>
                </div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    {patients.map((patient) => (
                        <div key={patient.id} className="card shadow-sm" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1.5rem' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '1.5rem' }}>
                                <div className="avatar-placeholder" style={{ margin: 0 }}>
                                    {patient.name.charAt(0)}
                                </div>
                                <div>
                                    <h3 style={{ margin: '0 0 0.25rem 0', color: 'var(--indigo-deep)' }}>{patient.name}</h3>
                                    <div style={{ display: 'flex', gap: '1rem', color: '#666', fontSize: '0.9rem' }}>
                                        <span>📧 {patient.email}</span>
                                        <span>🎂 {patient.age || 'N/A'} yrs</span>
                                        <span>🚻 {patient.gender || 'N/A'}</span>
                                    </div>
                                    <p style={{ margin: '0.5rem 0 0 0', fontSize: '0.85rem' }}>
                                        Condition: <span style={{ fontWeight: '500' }}>{patient.condition_name || 'General Inquiry'}</span>
                                    </p>
                                </div>
                            </div>
                            <div style={{ textAlign: 'right' }}>
                                <span style={{
                                    padding: '0.25rem 0.75rem',
                                    borderRadius: '20px',
                                    backgroundColor: '#e7f3ff',
                                    color: '#0061c1',
                                    fontWeight: 'bold',
                                    fontSize: '0.85rem'
                                }}>
                                    {patient.status}
                                </span>
                                <div style={{ marginTop: '0.75rem', fontSize: '0.8rem', color: '#666' }}>
                                    Last visited: {patient.date}
                                </div>
                                <div style={{ marginTop: '0.75rem', display: 'flex', gap: '0.5rem' }}>
                                    <button onClick={() => navigate(`/chat`)} className="btn btn-sm" style={{ fontSize: '0.75rem', padding: '0.25rem 0.5rem' }}>Chat</button>
                                    <button onClick={() => navigate(`/reports`)} className="btn btn-primary btn-sm" style={{ fontSize: '0.75rem', padding: '0.25rem 0.5rem', backgroundColor: 'var(--indigo-rich)' }}>Reports</button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default PatientsListPage;
