import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import StatCard from '../../components/common/StatCard';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { FiFileText, FiClock, FiTrendingUp, FiCheckCircle } from 'react-icons/fi';

const BankerDashboard = () => {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/banker/dashboard')
      .then(res => setData(res.data.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <DashboardLayout title="Dashboard"><LoadingSpinner /></DashboardLayout>;

  return (
    <DashboardLayout title="Dashboard" subtitle="Your implementation overview">
      <div className="space-y-6">

        {(data?.departmentName || data?.departmentHeadName) && (
          <div className="card p-5 flex items-center gap-4 border-l-4 border-brand-500">
            <div>
              <p className="text-xs text-slate-500 uppercase tracking-wide font-semibold">
                Your Department
              </p>
              <p className="text-base font-bold text-slate-900 mt-0.5">
                {data.departmentName || '—'}
              </p>
              {data.departmentHeadName && (
                <p className="text-xs text-slate-500 mt-0.5">
                  Head: {data.departmentHeadName}
                </p>
              )}
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <StatCard title="Total Assigned" value={data?.totalAssigned}    icon={<FiFileText />}    color="blue" />
          <StatCard title="Not Started"    value={data?.notImplemented}   icon={<FiClock />}       color="yellow" />
          <StatCard title="In Progress"    value={data?.inProgress}       icon={<FiTrendingUp />}  color="orange" />
          <StatCard title="Implemented"    value={data?.implemented}      icon={<FiCheckCircle />} color="green" />
        </div>

      </div>
    </DashboardLayout>
  );
};

export default BankerDashboard;