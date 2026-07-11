import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { getInitials } from '../../utils/helpers';

const DHBankers = () => {
  const [bankers, setBankers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/department-head/bankers')
      .then(res => setBankers(res.data.data || []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="My Bankers" subtitle="Active bankers in your department">
      <div className="card">
        {loading ? <LoadingSpinner /> : (
          <Table
            headers={['Banker', 'Email', 'Status', 'Department']}
            emptyMessage="No bankers in your department"
          >
            {bankers.map((b) => (
              <tr key={b.id} className="hover:bg-slate-50 transition-colors">
                <td className="table-cell">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-brand-100 rounded-full 
                                    flex items-center justify-center shrink-0">
                      <span className="text-brand-700 text-xs font-bold">
                        {getInitials(b.username)}
                      </span>
                    </div>
                    <span className="font-medium text-slate-900">{b.username}</span>
                  </div>
                </td>
                <td className="table-cell text-slate-500">{b.email}</td>
                <td className="table-cell"><Badge status={b.status} /></td>
                <td className="table-cell">
                  {b.departmentName
                    ? <span className="badge-blue">{b.departmentName}</span>
                    : '—'
                  }
                </td>
              </tr>
            ))}
          </Table>
        )}
      </div>
    </DashboardLayout>
  );
};

export default DHBankers;