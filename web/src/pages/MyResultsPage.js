import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { reportService } from '../services/api';

const MyResultsPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [results, setResults] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchResults = async () => {
            try {
                const response = await reportService.getPatientResults(user.id);
                if (response.data.status === 'success') {
                    setResults(response.data);
                } else {
                    setError(response.data.message);
                }
            } catch (err) {
                setError('Failed to fetch genetic results');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchResults();
    }, [user.id]);

    return (
        <div className="page-content">
            <div style={{ marginBottom: '2rem' }}>
                <h1 style={{ margin: 0, color: 'var(--teal-dark)' }}>My Genetic Results</h1>
                <p style={{ color: 'var(--text-sub)' }}>A comprehensive view of your clinical risk assessments and reports.</p>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>Retrieving your clinical data...</div>
            ) : error ? (
                <div className="card" style={{ color: 'var(--error)', textAlign: 'center' }}>{error}</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>

                    {/* Risk Summary Header */}
                    <div className="card shadow-sm" style={{
                        background: 'linear-gradient(135deg, var(--teal-main), var(--teal-dark))',
                        color: 'white',
                        padding: '2rem',
                        borderRadius: '24px'
                    }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <h4 style={{ margin: 0, opacity: 0.9 }}>Latest Risk Assessment</h4>
                                <h1 style={{ margin: '0.5rem 0', fontSize: '2.5rem' }}>{results.risk_assessment.risk_category}</h1>
                                <p style={{ margin: 0, opacity: 0.8 }}>Assessed on {new Date(results.risk_assessment.assessed_at).toLocaleDateString()}</p>
                            </div>
                            <div style={{
                                width: '100px',
                                height: '100px',
                                borderRadius: '50%',
                                border: '8px solid rgba(255,255,255,0.2)',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: '1.5rem',
                                fontWeight: 'bold'
                            }}>
                                {results.risk_assessment.risk_score}%
                            </div>
                        </div>
                    </div>

                    <div className="responsive-grid">
                        {/* Reports Section */}
                        <div className="card shadow-sm" style={{ gridColumn: '1 / span 2' }}>
                            <h3 style={{ marginBottom: '1.5rem', color: 'var(--teal-dark)' }}>Genetic Lab Reports</h3>
                            {results.reports.length === 0 ? (
                                <div style={{ textAlign: 'center', padding: '2rem', color: '#888' }}>
                                    Your finalized genetic reports will appear here once ready.
                                </div>
                            ) : (
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                    {results.reports.map((report, idx) => (
                                        <div key={idx} style={{
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'space-between',
                                            padding: '1rem',
                                            backgroundColor: '#f8f9fa',
                                            borderRadius: '12px'
                                        }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                                                <div style={{ fontSize: '1.5rem' }}>📄</div>
                                                <div>
                                                    <div style={{ fontWeight: 'bold' }}>{report.title}</div>
                                                    <div style={{ fontSize: '0.8rem', color: '#888' }}>{new Date(report.date).toLocaleDateString()}</div>
                                                </div>
                                            </div>
                                            <a
                                                href={report.file_url}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="btn btn-primary btn-sm"
                                                style={{ backgroundColor: 'var(--teal-main)', textDecoration: 'none' }}
                                            >
                                                View Report
                                            </a>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Analysis Card */}
                        <div className="card shadow-sm">
                            <h3 style={{ marginBottom: '1.5rem', color: 'var(--teal-dark)' }}>Next Steps</h3>
                            <p style={{ color: '#555', lineHeight: '1.6' }}>
                                Based on your results, we recommend a follow-up consultation to discuss clinical implications and personalized wellness planning.
                            </p>
                            <button
                                className="btn btn-primary"
                                style={{ width: '100%', marginTop: '1rem', backgroundColor: 'var(--teal-main)' }}
                                onClick={() => navigate('/book')}
                            >
                                Schedule Consultation
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MyResultsPage;
