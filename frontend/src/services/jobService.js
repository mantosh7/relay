import apiClient from './apiClient';

export const getAllJobs = async () => {
    const response = await apiClient.get('/api/v1/jobs');
    return response.data;
};

export const getJobById = async (id) => {
    const response = await apiClient.get(`/api/v1/jobs/${id}`);
    return response.data;
};

export const getFailedJobs = async () => {
    const response = await apiClient.get('/api/v1/jobs');
    return response.data.filter((job) => job.status === 'FAILED');
};