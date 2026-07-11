import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import StatCard from '../../components/common/StatCard';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { FiUpload, FiTrendingUp, FiCheckCircle, FiAward } from 'react-icons/fi';

const VendorDashboard = () => {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/vendor/dashboard')
      .then(res => setData(res.data.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <DashboardLayout title="Dashboard"><LoadingSpinner /></DashboardLayout>;

  return (
    <DashboardLayout title="Dashboard" subtitle="Your recommendation overview">
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard title="Total Uploads"   value={data?.totalUploads}              icon={<FiUpload />}      color="blue" />
        <StatCard title="Assigned"        value={data?.assignedRecommendations}   icon={<FiTrendingUp />}  color="yellow" />
        <StatCard title="Implemented"     value={data?.implementedRecommendations} icon={<FiCheckCircle />} color="purple" />
        <StatCard title="Verified"        value={data?.verifiedRecommendations}   icon={<FiAward />}       color="green" />
      </div>
    </DashboardLayout>
  );
};

export default VendorDashboard;