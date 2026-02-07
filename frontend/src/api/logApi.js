import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api/logs",
});

export const getRootCauses = () => api.get("/root-causes");
export const getExplanation = (groupId) => api.get(`/explain/${groupId}`);
export const getErrors = () => api.get("/errors");
export const getAnomaly = (service) => api.get(`/anomaly/${service}`);

export default api;
