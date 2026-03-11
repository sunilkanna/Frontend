import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { riskService } from '../services/api';
import { useNavigate } from 'react-router-dom';

const RiskAssessmentPage = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [step, setStep] = useState(1);
    const [answers, setAnswers] = useState({
        family_history: '',
        age: '',
        symptoms: [],
        previous_genetic_tests: ''
    });

    const calculateRisk = () => {
        let score = 0;
        if (answers.family_history === 'Yes') score += 5;
        if (parseInt(answers.age) > 50) score += 3;
        score += answers.symptoms.length * 2;

        let category = 'Low';
        if (score > 10) category = 'High';
        else if (score > 5) category = 'Moderate';

        return { score, category };
    };

    const handleSubmit = async () => {
        const { score, category } = calculateRisk();
        try {
            const response = await riskService.saveAssessment({
                patient_id: user.id,
                risk_score: score,
                risk_category: category,
                details: answers
            });
            if (response.data.status === 'success') {
                alert(`Assessment Complete! Your risk category is: ${category}`);
                navigate('/dashboard');
            }
        } catch (err) {
            alert('Failed to save assessment');
        }
    };

    return (
        <div className="page-content" style={{ maxWidth: '800px', margin: '0 auto' }}>
            <div className="card shadow-lg" style={{ padding: '3rem' }}>
                <h1 style={{ textAlign: 'center', color: 'var(--teal-dark)', marginBottom: '0.5rem' }}>Genetic Risk Assessment</h1>
                <p style={{ textAlign: 'center', color: 'var(--text-sub)', marginBottom: '3rem' }}>
                    Step {step} of 3
                </p>

                {step === 1 && (
                    <div>
                        <h3>Family History</h3>
                        <p>Does anyone in your immediate family have a history of genetic disorders?</p>
                        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                            <button className={`btn ${answers.family_history === 'Yes' ? 'btn-primary' : ''}`} onClick={() => setAnswers({ ...answers, family_history: 'Yes' })}>Yes</button>
                            <button className={`btn ${answers.family_history === 'No' ? 'btn-primary' : ''}`} onClick={() => setAnswers({ ...answers, family_history: 'No' })}>No</button>
                        </div>
                    </div>
                )}

                {step === 2 && (
                    <div>
                        <h3>Age & Symptoms</h3>
                        <div className="input-group">
                            <label>Current Age</label>
                            <input type="number" value={answers.age} onChange={e => setAnswers({ ...answers, age: e.target.value })} />
                        </div>
                        <p style={{ marginTop: '1rem' }}>Select any symptoms you are experiencing:</p>
                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem' }}>
                            {['Fatigue', 'Joint Pain', 'Vision Issues', 'Memory Loss'].map(s => (
                                <label key={s} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                    <input
                                        type="checkbox"
                                        checked={answers.symptoms.includes(s)}
                                        onChange={e => {
                                            const newSymptoms = e.target.checked
                                                ? [...answers.symptoms, s]
                                                : answers.symptoms.filter(item => item !== s);
                                            setAnswers({ ...answers, symptoms: newSymptoms });
                                        }}
                                    />
                                    {s}
                                </label>
                            ))}
                        </div>
                    </div>
                )}

                {step === 3 && (
                    <div>
                        <h3>Final Review</h3>
                        <p>Have you had any previous genetic testing?</p>
                        <select
                            style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #dfe1e6' }}
                            value={answers.previous_genetic_tests}
                            onChange={e => setAnswers({ ...answers, previous_genetic_tests: e.target.value })}
                        >
                            <option value="">Select...</option>
                            <option value="Yes">Yes</option>
                            <option value="No">No</option>
                            <option value="Not Sure">Not Sure</option>
                        </select>
                    </div>
                )}

                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '3rem' }}>
                    {step > 1 && <button className="btn" onClick={() => setStep(step - 1)}>Previous</button>}
                    {step < 3 ? (
                        <button className="btn btn-primary" style={{ marginLeft: 'auto' }} onClick={() => setStep(step + 1)}>Next</button>
                    ) : (
                        <button className="btn btn-primary" style={{ marginLeft: 'auto' }} onClick={handleSubmit}>Submit Assessment</button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default RiskAssessmentPage;
