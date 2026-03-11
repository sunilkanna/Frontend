import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const ProfileSetupPage = () => {
    const { user } = useAuth();
    const [formData, setFormData] = useState({
        phone: '',
        address: '',
        date_of_birth: '',
        gender: 'Male',
        specialization: '',
        bio: '',
        experience_years: '',
        consultation_fee: ''
    });
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setSuccess(false);

        try {
            let endpoint = user.user_type === 'Patient' ? 'update_profile.php' : 'save_counselor_profile.php';
            const response = await api.post(endpoint, { ...formData, user_id: user.id });

            if (response.data.status === 'success') {
                setSuccess(true);
            } else {
                alert(response.data.message || 'Update failed');
            }
        } catch (err) {
            alert('An error occurred');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page-content">
            <div className="card shadow-sm" style={{ maxWidth: '800px', margin: '0 auto', padding: '3rem' }}>
                <div style={{ marginBottom: '2rem' }}>
                    <h1 style={{ margin: 0, color: 'var(--teal-dark)' }}>Account Settings</h1>
                    <p style={{ color: 'var(--text-sub)' }}>Manage your personal information and preferences.</p>
                </div>

                {success && <div style={{ color: 'var(--success)', marginBottom: '1rem', fontWeight: 'bold' }}>Profile updated successfully!</div>}

                <form onSubmit={handleSubmit}>
                    {user.user_type === 'Patient' ? (
                        <>
                            <div className="input-group">
                                <label>Phone Number</label>
                                <input type="text" name="phone" value={formData.phone} onChange={handleChange} placeholder="e.g. +91 9876543210" />
                            </div>
                            <div className="input-group">
                                <label>Date of Birth</label>
                                <input type="date" name="date_of_birth" value={formData.date_of_birth} onChange={handleChange} />
                            </div>
                            <div className="input-group">
                                <label>Gender</label>
                                <select name="gender" value={formData.gender} onChange={handleChange} style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #dfe1e6' }}>
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div className="input-group">
                                <label>Address</label>
                                <textarea
                                    name="address"
                                    value={formData.address}
                                    onChange={handleChange}
                                    rows="3"
                                    style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #dfe1e6' }}
                                ></textarea>
                            </div>
                        </>
                    ) : (
                        <>
                            <div className="input-group">
                                <label>Specialization</label>
                                <input type="text" name="specialization" value={formData.specialization} onChange={handleChange} placeholder="e.g. Cancer Genetics" />
                            </div>
                            <div className="input-group">
                                <label>Experience (Years)</label>
                                <input type="number" name="experience_years" value={formData.experience_years} onChange={handleChange} />
                            </div>
                            <div className="input-group">
                                <label>Consultation Fee (₹)</label>
                                <input type="number" name="consultation_fee" value={formData.consultation_fee} onChange={handleChange} />
                            </div>
                            <div className="input-group">
                                <label>Bio</label>
                                <textarea
                                    name="bio"
                                    value={formData.bio}
                                    onChange={handleChange}
                                    rows="4"
                                    style={{ width: '100%', padding: '0.75rem', borderRadius: '8px', border: '1px solid #dfe1e6' }}
                                ></textarea>
                            </div>
                        </>
                    )}

                    <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }} disabled={loading}>
                        {loading ? 'Saving...' : 'Save Profile'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default ProfileSetupPage;
