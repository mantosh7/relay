import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login, loginWithGoogle } from '../services/authService';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { loginUser } = useAuth();

    // Handles form submission for email/password login
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const data = await login(email, password);
            loginUser(data);
            navigate('/dashboard');
        } catch (err) {
            setError('Invalid email or password');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-96">
                <h1 className="text-2xl font-bold mb-6 text-center">Relay Login</h1>

                {error && (
                    <div className="bg-red-100 text-red-700 p-2 rounded mb-4 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="w-full border p-2 rounded mb-3"
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="w-full border p-2 rounded mb-4"
                        required
                    />
                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
                    >
                        Login
                    </button>
                </form>

                <button
                    onClick={loginWithGoogle}
                    className="w-full mt-3 border p-2 rounded hover:bg-gray-50"
                >
                    Sign in with Google
                </button>

                <p className="text-center mt-4 text-sm">
                    Don't have an account? <Link to="/signup" className="text-blue-600">Sign up</Link>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;