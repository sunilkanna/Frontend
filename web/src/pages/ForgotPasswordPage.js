import React, { useState } from 'react';
import { otpService } from '../services/api';
import { useNavigate } from 'react-router-dom';

const ForgotPasswordPage = () => {
    const [email, setEmail] = useState('');
    const [otp, setOtp] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [step, setStep] = useState(1); // 1: Email, 2: OTP, 3: New Password
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSendOTP = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await otpService.sendOTP(email);
            if (res.data.status === 'success') {
                alert(res.data.message);
                setStep(2);
            } else {
                alert(res.data.message);
            }
        } catch (err) {
            alert('Failed to send OTP');
        } finally {
            setLoading(false);
        }
    };

    const handleVerifyOTP = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await otpService.verifyOTP(email, otp);
            if (res.data.status === 'success') {
                setStep(3);
            } else {
                alert(res.data.message);
            }
        } catch (err) {
            alert('Verification failed');
        } finally {
            setLoading(false);
        }
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await otpService.resetPassword(email, newPassword);
            if (res.data.status === 'success') {
                alert('Password reset successfully!');
                navigate('/login');
            } else {
                alert(res.data.message);
            }
        } catch (err) {
            alert('Reset failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container" style={{ padding: '4rem 1rem', maxWidth: '400px' }}>
            <div className="card">
                <h2 style={{ textAlign: 'center', marginBottom: '2rem' }}>Reset Password</h2>

                {step === 1 && (
                    <form onSubmit={handleSendOTP}>
                        <div className="input-group">
                            <label>Email Address</label>
                            <input
                                type="email"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary" style={{ width: '100%' }} disabled={loading}>
                            {loading ? 'Sending...' : 'Send Verification Code'}
                        </button>
                    </form>
                )}

                {step === 2 && (
                    <form onSubmit={handleVerifyOTP}>
                        <div className="input-group">
                            <label>Verification Code</label>
                            <input
                                type="text"
                                value={otp}
                                onChange={e => setOtp(e.target.value)}
                                required
                                maxLength="5"
                                style={{ textAlign: 'center', letterSpacing: '4px', fontSize: '1.5rem' }}
                            />
                        </div>
                        <button type="submit" className="btn btn-primary" style={{ width: '100%' }} disabled={loading}>
                            {loading ? 'Verifying...' : 'Verify Code'}
                        </button>
                    </form>
                )}

                {step === 3 && (
                    <form onSubmit={handleResetPassword}>
                        <div className="input-group">
                            <label>New Password</label>
                            <input
                                type="password"
                                value={newPassword}
                                onChange={e => setNewPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary" style={{ width: '100%' }} disabled={loading}>
                            {loading ? 'Resetting...' : 'Reset Password'}
                        </button>
                    </form>
                )}

                <div style={{ textAlign: 'center', marginTop: '1.5rem' }}>
                    <button className="btn" style={{ background: 'none' }} onClick={() => navigate('/login')}>
                        Back to Login
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ForgotPasswordPage;
