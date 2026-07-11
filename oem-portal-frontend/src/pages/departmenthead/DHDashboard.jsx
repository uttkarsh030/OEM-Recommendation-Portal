import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import StatCard from '../../components/common/StatCard';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { FiInbox, FiEye, FiUsers, FiCheckSquare, FiCheckCircle } from 'react-icons/fi';

const DHDashboard = () => {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/department-head/dashboard')
      .then(res => setData(res.data.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <DashboardLayout title="Dashboard"><LoadingSpinner /></DashboardLayout>;

  return (
    <DashboardLayout
      title={data?.departmentName ? `${data.departmentName}` : 'Dashboard'}
      subtitle="Department Head overview"
    >
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <StatCard title="New Assignments"  value={data?.assignedRecommendations}    icon={<FiInbox />}       color="blue" />
        <StatCard title="Under Review"     value={data?.underReviewRecommendations} icon={<FiEye />}         color="yellow" />
        <StatCard title="Sent to Bankers"  value={data?.assignedToBankers}          icon={<FiUsers />}       color="purple" />
        <StatCard title="Implemented"      value={data?.implementedRecommendations} icon={<FiCheckSquare />} color="indigo" />
        <StatCard title="Reviewed"         value={data?.reviewedRecommendations}    icon={<FiCheckCircle />} color="green" />
      </div>
    </DashboardLayout>
  );
};

export default DHDashboard;