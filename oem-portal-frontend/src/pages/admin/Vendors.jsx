import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';

const Vendors = () => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/vendors')
      .then(res => setVendors(res.data.data || []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="Vendors" subtitle="All registered vendors">
      <div className="card">
        {loading ? <LoadingSpinner /> : (
          <Table
            headers={['Vendor', 'Email', 'Phone', 'Recommendations']}
            emptyMessage="No vendors registered"
          >
            {vendors.map((v) => (
              <tr key={v.id} className="hover:bg-slate-50 transition-colors">
                <td className="table-cell">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-purple-100 rounded-full 
                                    flex items-center justify-center shrink-0">
                      <span className="text-purple-700 text-xs font-bold">
                        {v.name?.charAt(0).toUpperCase()}
                      </span>
                    </div>
                    <span className="font-medium text-slate-900">{v.name}</span>
                  </div>
                </td>
                <td className="table-cell text-slate-500">{v.email}</td>
                <td className="table-cell text-slate-500">{v.phone}</td>
                <td className="table-cell">
                  <span className="badge-blue">
                    {v.totalRecommendations} uploads
                  </span>
                </td>
              </tr>
            ))}
          </Table>
        )}
      </div>
    </DashboardLayout>
  );
};

export default Vendors;