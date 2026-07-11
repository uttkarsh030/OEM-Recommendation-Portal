import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { formatDate } from '../../utils/helpers';
import { FiPlay, FiCheckCircle } from 'react-icons/fi';

const BankerRecommendations = () => {
  const [recs, setRecs]         = useState([]);
  const [loading, setLoading]   = useState(true);
  const [updating, setUpdating] = useState(null);

  const fetchRecs = () => {
    api.get('/banker/recommendations')
      .then(res => setRecs(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchRecs(); }, []);

  const updateStatus = async (id, status) => {
    setUpdating(id);
    try {
      await api.put(`/banker/recommendations/${id}/status`, { status });
      fetchRecs();
    } finally {
      setUpdating(null);
    }
  };

  const getAction = (status, id) => {
    if (status === 'ASSIGNED') {
      return (
        <button
          onClick={() => updateStatus(id, 'IN_PROGRESS')}
          disabled={updating === id}
          className="btn-primary text-xs py-1.5"
        >
          <FiPlay size={12} />
          {updating === id ? 'Updating...' : 'Start Work'}
        </button>
      );
    }
    if (status === 'IN_PROGRESS') {
      return (
        <button
          onClick={() => updateStatus(id, 'IMPLEMENTED')}
          disabled={updating === id}
          className="btn-success text-xs py-1.5"
        >
          <FiCheckCircle size={12} />
          {updating === id ? 'Updating...' : 'Mark Done'}
        </button>
      );
    }
    if (status === 'IMPLEMENTED') {
      return (
        <span className="text-xs text-slate-400 flex items-center gap-1">
          <FiCheckCircle size={12} className="text-emerald-500" />
          Awaiting Review
        </span>
      );
    }
    return '—';
  };

  return (
    <DashboardLayout
      title="My Recommendations"
      subtitle="Recommendations assigned to you"
    >
      <div className="card">
        {loading ? <LoadingSpinner /> : (
          <Table
            headers={['Title', 'OEM', 'Product', 'Application', 'Status', 'Date', 'Action']}
            emptyMessage="No recommendations assigned to you yet"
          >
            {recs.map((r) => (
              <tr key={r.id} className="hover:bg-slate-50 transition-colors">
                <td className="table-cell">
                  <p className="font-medium text-slate-900 max-w-[150px] truncate">
                    {r.title}
                  </p>
                  <p className="text-xs text-slate-400">#{r.id}</p>
                </td>
                <td className="table-cell text-slate-600">{r.oemName || '—'}</td>
                <td className="table-cell text-slate-500">{r.productName || '—'}</td>
                <td className="table-cell text-slate-500">{r.applicationName || '—'}</td>
                <td className="table-cell"><Badge status={r.status} /></td>
                <td className="table-cell text-slate-400 text-xs">
                  {formatDate(r.uploadDate)}
                </td>
                <td className="table-cell">{getAction(r.status, r.id)}</td>
              </tr>
            ))}
          </Table>
        )}
      </div>
    </DashboardLayout>
  );
};

export default BankerRecommendations;