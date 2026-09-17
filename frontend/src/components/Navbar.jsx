import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
    const { logoutUser } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logoutUser();
        navigate('/login');
    };

    return (
        <nav className="bg-white shadow px-6 py-4 flex justify-between items-center">
            <h1 className="text-xl font-bold">Relay</h1>
            <div className="flex gap-4 items-center">
                <Link to="/dashboard" className="text-gray-700 hover:text-blue-600">Jobs</Link>
                <Link to="/dlq" className="text-gray-700 hover:text-blue-600">Dead Letter Queue</Link>
                <button onClick={handleLogout} className="text-red-600">Logout</button>
            </div>
        </nav>
    );
}

export default Navbar;