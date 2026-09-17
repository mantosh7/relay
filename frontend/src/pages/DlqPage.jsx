import { useState, useEffect } from 'react';
import { getFailedJobs } from '../services/jobService';
import Navbar from '../components/Navbar';

function DlqPage() {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchFailedJobs();
    }, []);

    const fetchFailedJobs = async () => {
        try {
            const data = await getFailedJobs();
            setJobs(data);
        } catch (err) {
            console.error('Failed to fetch DLQ jobs', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <Navbar />
            <div className="p-6">
                <h1 className="text-2xl font-bold mb-6">Dead Letter Queue</h1>

                {loading ? (
                    <p>Loading...</p>
                ) : jobs.length === 0 ? (
                    <p className="text-gray-500">No failed jobs.</p>
                ) : (
                    <table className="w-full bg-white rounded shadow">
                        <thead>
                        <tr className="border-b text-left">
                            <th className="p-3">Job Type</th>
                            <th className="p-3">Retries</th>
                            <th className="p-3">Error</th>
                            <th className="p-3">Created At</th>
                        </tr>
                        </thead>
                        <tbody>
                        {jobs.map((job) => (
                            <tr key={job.jobId} className="border-b">
                                <td className="p-3">{job.jobType}</td>
                                <td className="p-3">{job.retryCount}</td>
                                <td className="p-3 text-red-600 text-sm">{job.errorMessage || '-'}</td>
                                <td className="p-3">{new Date(job.createdAt).toLocaleString()}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default DlqPage;