import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { appointmentService, counselorService } from '../services/api';

const AppointmentsPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAppointments = async () => {
            try {
                let response;
                if (user.user_type === 'Counselor') {
                    response = await counselorService.getAppointments(user.id);
                } else {
                    response = await appointmentService.getAppointments(user.id);
                }

                if (response.data.status === 'success') {
                    setAppointments(response.data.appointments);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch appointments');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchAppointments();
    }, [user.id, user.user_type]);

    const getStatusColor = (status) => {
        switch (status) {
            case 'Confirmed': return '#00c853';
            case 'Pending': return '#ffab00';
            case 'Cancelled': return '#de350b';
            case 'Completed': return '#00acc1';
            default: return '#666';
        }
    };

    const isCounselor = user.user_type === 'Counselor';

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: isCounselor ? 'var(--indigo-deep)' : 'var(--teal-dark)' }}>
                    {isCounselor ? 'My Sessions' : 'My Appointments'}
                </h1>
                <p style={{ color: 'var(--text-sub)' }}>
                    {isCounselor ? 'Manage your upcoming consultations and patient schedule.' : 'View and manage your upcoming and past genetic consultations.'}
                </p>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>Loading sessions...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)', textAlign: 'center' }}>{error}</div>
            ) : appointments.length === 0 ? (
                <div className="card" style={{ textAlign: 'center', padding: '4rem' }}>
                    <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🗓️</div>
                    <h3>No Appointments Found</h3>
                    <p style={{ color: '#666' }}>{isCounselor ? "You don't have any appointments scheduled yet." : "You haven't booked any sessions yet."}</p>
                    {!isCounselor && <button className="btn btn-primary" onClick={() => navigate('/book')} style={{ marginTop: '1rem' }}>Book Now</button>}
                </div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    {appointments.map((appt) => (
                        <div key={appt.id} className="card shadow-sm" style={{ padding: '1.5rem', borderLeft: `5px solid ${getStatusColor(appt.status)}` }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <div>
                                    <h3 style={{ margin: '0 0 0.5rem 0' }}>
                                        {isCounselor ? appt.patient_name : `Dr. ${appt.counselor_name}`}
                                    </h3>
                                    <div style={{ display: 'flex', gap: '1rem', color: '#666', fontSize: '0.9rem' }}>
                                        <span>📅 {appt.appointment_date}</span>
                                        <span>⏰ {appt.time_slot}</span>
                                    </div>
                                    {isCounselor && <p style={{ margin: '0.5rem 0 0', fontSize: '0.85rem', color: 'var(--text-sub)' }}>Patient: {appt.patient_email}</p>}
                                </div>
                                <div style={{ textAlign: 'right' }}>
                                    <span style={{
                                        padding: '0.25rem 0.75rem',
                                        borderRadius: '20px',
                                        backgroundColor: getStatusColor(appt.status) + '22',
                                        color: getStatusColor(appt.status),
                                        fontWeight: 'bold',
                                        fontSize: '0.85rem'
                                    }}>
                                        {appt.status}
                                    </span>
                                    {appt.status === 'Confirmed' && (
                                        <div style={{ marginTop: '1rem' }}>
                                            <button
                                                className="btn btn-primary"
                                                onClick={() => navigate(`/video-call/${appt.id}`)}
                                                style={{ padding: '0.4rem 1rem', fontSize: '0.9rem', backgroundColor: isCounselor ? '#3f51b5' : '' }}
                                            >
                                                {isCounselor ? 'Start Session' : 'Join Now'}
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default AppointmentsPage;
