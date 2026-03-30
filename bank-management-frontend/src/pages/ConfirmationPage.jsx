import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';  // Updated import to use `useNavigate`
import axios from 'axios';

const useQuery = () => {
    return new URLSearchParams(useLocation().search);
};

const ConfirmationPage = () => {
    const query = useQuery();
    const token = query.get('token');
    const navigate = useNavigate();  // Replace `useHistory` with `useNavigate`

    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        if (token) {
            confirmEmail(token);
        }
    }, [token]);

    const confirmEmail = async (token) => {
        try {
            const response = await axios.get(`/api/auth/confirm`, { params: { token } });
            setMessage('Your email has been confirmed successfully! You can now log in.');
            setTimeout(() => navigate('/login'), 3000);  // Use `navigate` instead of `history.push`
        } catch (err) {
            setError('Invalid or expired confirmation link.');
        }
    };

    return (
        <div>
            <h1>Email Confirmation</h1>
            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default ConfirmationPage;
