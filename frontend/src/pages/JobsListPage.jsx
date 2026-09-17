import { useState, useEffect } from 'react';
import { getAllJobs } from '../services/jobService';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

function JobsListPage() {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const { logoutUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        fetchJobs();
    }, []);

    const fetchJobs = async () => {
        try {
            const data = await getAllJobs();
            setJobs(data);
        } catch (err) {
            console.error('Failed to fetch jobs', err);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        logoutUser();
        navigate('/login');
    };

    const statusColor = (status) => {
        if (status === 'SUCCESS') return 'bg-green-100 text-green-700';
        if (status === 'FAILED') return 'bg-red-100 text-red-700';
        if (status === 'PROCESSING') return 'bg-yellow-100 text-yellow-700';
        return 'bg-gray-100 text-gray-700';
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="p-6">
                <div className="min-h-screen bg-gray-100 p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-2xl font-bold">Jobs Dashboard</h1>
                        <button onClick={handleLogout} className="text-red-600">Logout</button>
                    </div>

                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        <table className="w-full bg-white rounded shadow">
                            <thead>
                            <tr className="border-b text-left">
                                <th className="p-3">Job Type</th>
                                <th className="p-3">Priority</th>
                                <th className="p-3">Status</th>
                                <th className="p-3">Retries</th>
                                <th className="p-3">Created At</th>
                            </tr>
                            </thead>
                            <tbody>
                            {jobs.map((job) => (
                                <tr key={job.jobId} className="border-b">
                                    <td className="p-3">{job.jobType}</td>
                                    <td className="p-3">{job.priority}</td>
                                    <td className="p-3">
                  <span className={`px-2 py-1 rounded text-xs ${statusColor(job.status)}`}>
                    {job.status}
                  </span>
                                    </td>
                                    <td className="p-3">{job.retryCount}</td>
                                    <td className="p-3">{new Date(job.createdAt).toLocaleString()}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>



    );
}

export default JobsListPage;