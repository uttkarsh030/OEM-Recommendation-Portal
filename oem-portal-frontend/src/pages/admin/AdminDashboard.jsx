import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import StatCard from '../../components/common/StatCard';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import {
  FiShield, FiUsers, FiClock, FiPackage,
  FiFileText, FiCheckCircle, FiAlertCircle, FiTrendingUp
} from 'react-icons/fi';

const AdminDashboard = () => {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/dashboard')
      .then(res => setData(res.data.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return (
    <DashboardLayout title="Dashboard">
      <LoadingSpinner />
    </DashboardLayout>
  );

  const statuses = [
    { label: 'Awaiting Assignment', value: data?.uploadedRecommendations ?? 0, color: 'bg-slate-400' },
    { label: 'In Progress',         value: data?.assignedRecommendations  ?? 0, color: 'bg-brand-500' },
    { label: 'Awaiting Verify',     value: data?.reviewedRecommendations  ?? 0, color: 'bg-indigo-500' },
    { label: 'Verified',            value: data?.verifiedRecommendations  ?? 0, color: 'bg-emerald-500' },
  ];

  const summary = [
    {
      label: 'Total Recommendations',
      value: data?.totalRecommendations   ?? 0,
      icon:  FiFileText,
      iconCx: 'text-brand-600',
      bg:    'bg-brand-50',
      border:'border-brand-100',
    },
    {
      label: 'Verified',
      value: data?.verifiedRecommendations ?? 0,
      icon:  FiCheckCircle,
      iconCx:'text-emerald-600',
      bg:    'bg-emerald-50',
      border:'border-emerald-100',
    },
    {
      label: 'Pending Approval',
      value: data?.pendingApprovals        ?? 0,
      icon:  FiAlertCircle,
      iconCx:'text-amber-600',
      bg:    'bg-amber-50',
      border:'border-amber-100',
    },
    {
      label: 'Dept Heads',
      value: data?.totalDepartmentHeads    ?? 0,
      icon:  FiTrendingUp,
      iconCx:'text-purple-600',
      bg:    'bg-purple-50',
      border:'border-purple-100',
    },
  ];

  return (
    <DashboardLayout
      title="Dashboard"
      subtitle="Overview of your OEM portal"
    >
      <div className="space-y-6">

        {/* Stat cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <StatCard title="Departments"       value={data?.totalDepartments} icon={<FiShield />}  color="blue"   />
          <StatCard title="Total Bankers"     value={data?.totalBankers}     icon={<FiUsers />}   color="indigo" />
          <StatCard title="Pending Approvals" value={data?.pendingApprovals} icon={<FiClock />}   color="yellow" />
          <StatCard title="Total Vendors"     value={data?.totalVendors}     icon={<FiPackage />} color="purple" />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">

          {/* Pipeline */}
          <div className="lg:col-span-2 card p-6">
            <h3 className="text-sm font-semibold text-slate-900 mb-5">
              Recommendation Pipeline
            </h3>
            <div className="space-y-4">
              {statuses.map((s) => {
                const total = data?.totalRecommendations || 1;
                const pct   = Math.round((s.value / total) * 100);
                return (
                  <div key={s.label}>
                    <div className="flex justify-between mb-2">
                      <span className="text-sm font-medium text-slate-700">
                        {s.label}
                      </span>
                      <span className="text-sm font-bold text-slate-900 tabular-nums">
                        {s.value}
                      </span>
                    </div>
                    <div className="h-2 bg-slate-100 rounded-full overflow-hidden">
                      <div
                        className={`h-full ${s.color} rounded-full transition-all duration-500`}
                        style={{ width: `${pct}%` }}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Quick Summary */}
          <div className="card p-6">
            <h3 className="text-sm font-semibold text-slate-900 mb-5">
              Quick Summary
            </h3>
            <div className="space-y-3">
              {summary.map((item) => {
                const Icon = item.icon;
                return (
                  <div
                    key={item.label}
                    className={`flex items-center justify-between
                                p-3 rounded-xl ${item.bg}
                                border ${item.border}`}
                  >
                    <div className="flex items-center gap-3">
                      <div className={`p-2 rounded-lg bg-white shadow-sm`}>
                        <Icon size={15} className={item.iconCx} />
                      </div>
                      <span className="text-sm font-medium text-slate-700">
                        {item.label}
                      </span>
                    </div>
                    <span className="text-base font-bold text-slate-900 tabular-nums">
                      {item.value}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
};

export default AdminDashboard;