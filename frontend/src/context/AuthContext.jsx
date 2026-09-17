import { createContext, useContext, useState, useEffect } from 'react';
import apiClient from '../services/apiClient';

// Create the context object that will hold auth state
const AuthContext = createContext();

// Provider component wraps the entire app and supplies auth state to all children
export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // Check if user is already logged in when app loads (cookie might already exist)
    useEffect(() => {
        checkAuthStatus();
    }, []);

    const checkAuthStatus = async () => {
        try {
            const response = await apiClient.get('/auth/me');
            setUser(response.data);
        } catch (error) {
            setUser(null);
        } finally {
            setLoading(false);
        }
    };

    const loginUser = (userData) => {
        setUser(userData);
    };

    const logoutUser = () => {
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, loading, loginUser, logoutUser }}>
            {children}
        </AuthContext.Provider>
    );
}

// Custom hook so components can easily access auth state
export function useAuth() {
    return useContext(AuthContext);
}