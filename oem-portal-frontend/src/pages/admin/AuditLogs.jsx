import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { formatDate } from '../../utils/helpers';

const actionColors = {
  RECOMMENDATION_UPLOADED:            'bg-slate-100 text-slate-600',
  RECOMMENDATION_ASSIGNED_TO_DEPARTMENT: 'bg-blue-50 text-blue-700',
  RECOMMENDATION_VERIFIED:            'bg-emerald-50 text-emerald-700',
  BANKER_APPROVED:                    'bg-emerald-50 text-emerald-700',
  BANKER_REJECTED:                    'bg-red-50 text-red-700',
  STATUS_UPDATED:                     'bg-orange-50 text-orange-700',
  RECOMMENDATION_ASSIGNED_TO_BANKER:  'bg-purple-50 text-purple-700',
  IMPLEMENTATION_REVIEWED:            'bg-indigo-50 text-indigo-700',
};

const AuditLogs = () => {
  const [logs, setLogs]       = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/audit-logs')
      .then(res => setLogs(res.data.data || []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="Audit Logs" subtitle="Complete system activity trail">
      <div className="card">
        {loading ? <LoadingSpinner /> : (
          <Table
            headers={['Action', 'Performed By', 'Role', 'Rec ID', 'Transition', 'Time']}
            emptyMessage="No audit logs found"
          >
            {logs.map((l) => (
              <tr key={l.id} className="hover:bg-slate-50 transition-colors">
                <td className="table-cell">
                  <span className={`text-xs font-mono font-medium px-2 py-1 
                                    rounded-md ${actionColors[l.action] || 'badge-gray'}`}>
                    {l.action?.replace(/_/g, ' ')}
                  </span>
                </td>
                <td className="table-cell">
                  <p className="text-sm font-medium text-slate-700">
                    {l.performedBy}
                  </p>
                </td>
                <td className="table-cell">
                  <span className="badge-blue text-xs">{l.role}</span>
                </td>
                <td className="table-cell text-slate-500 text-xs">
                  {l.recommendationId ? `#${l.recommendationId}` : '—'}
                </td>
                <td className="table-cell">
                  {l.previousStatus || l.newStatus ? (
                    <div className="flex items-center gap-1.5 text-xs">
                      {l.previousStatus && (
                        <span className="badge-gray">{l.previousStatus}</span>
                      )}
                      {l.previousStatus && l.newStatus && (
                        <span className="text-slate-300">→</span>
                      )}
                      {l.newStatus && (
                        <span className="badge-green">{l.newStatus}</span>
                      )}
                    </div>
                  ) : '—'}
                </td>
                <td className="table-cell text-slate-400 text-xs">
                  {formatDate(l.timestamp)}
                </td>
              </tr>
            ))}
          </Table>
        )}
      </div>
    </DashboardLayout>
  );
};

export default AuditLogs;