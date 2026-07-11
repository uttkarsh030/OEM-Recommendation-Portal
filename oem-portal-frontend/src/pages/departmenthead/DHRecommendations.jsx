import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { formatDate } from '../../utils/helpers';
import { FiFileText, FiUserCheck, FiClipboard } from 'react-icons/fi';

const DHRecommendations = () => {
  const [tab, setTab]           = useState('assigned');
  const [recs, setRecs]         = useState([]);
  const [loading, setLoading]   = useState(true);
  const [bankers, setBankers]   = useState([]);
  const [assignModal, setAssignModal] = useState(null);
  const [selectedBanker, setSelectedBanker] = useState('');

  const tabs = [
    { key: 'assigned',    label: 'New',         url: '/department-head/recommendations/assigned' },
    { key: 'implemented', label: 'Implemented', url: '/department-head/recommendations/implemented' },
  ];

  const fetchRecs = () => {
    setLoading(true);
    const current = tabs.find(t => t.key === tab);
    api.get(current.url)
      .then(res => setRecs(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchRecs(); }, [tab]);

  useEffect(() => {
    api.get('/department-head/bankers')
      .then(res => setBankers(res.data.data || []));
  }, []);

  // FIXED: Two-step process - Review first, then open assign modal
// Just opens the modal — no side effects, no API calls
  const handleAssignFlow = (rec) => {
    setAssignModal(rec);
    setSelectedBanker('');
  };

  // Does the review transition (if needed) AND the assignment together,
  // only when the user actually confirms
  const handleAssignBanker = async () => {
    if (!selectedBanker) return;
    try {
      // Move to UNDER_REVIEW first if it's still fresh
      if (assignModal.status === 'DEPARTMENT_ASSIGNED') {
        await api.put(`/department-head/recommendations/${assignModal.id}/review`);
      }
      await api.post('/department-head/recommendations/assign-banker', {
        recommendationId: assignModal.id,
        bankerId:         Number(selectedBanker),
      });
      setAssignModal(null);
      setSelectedBanker('');
      fetchRecs();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to assign banker');
    }
  };

  const handleReviewImplementation = async (id) => {
    await api.put(`/department-head/recommendations/${id}/review-implementation`);
    fetchRecs();
  };

  return (
    <DashboardLayout
      title="Recommendations"
      subtitle="Review and assign recommendations to your bankers"
    >
      <div className="space-y-6">

        {/* Header row (matches AdminDashboard header pattern) */}
        <div className="flex items-center gap-3">
          <div className="p-2.5 rounded-xl bg-brand-50 border border-brand-100">
            <FiFileText size={18} className="text-brand-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-900">Recommendations</h1>
            <p className="text-slate-500 text-sm mt-0.5">
              Review and assign recommendations
            </p>
          </div>
        </div>

        <div className="card p-6">
          {/* Tabs */}
          <div className="flex gap-2 mb-5">
            {tabs.map((t) => (
              <button
                key={t.key}
                onClick={() => setTab(t.key)}
                className={`px-4 py-1.5 rounded-lg text-sm font-medium transition-colors
                  ${tab === t.key
                    ? 'bg-brand-600 text-white shadow-sm'
                    : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                  }`}
              >
                {t.label}
              </button>
            ))}
          </div>

          {loading ? <LoadingSpinner /> : (
            <div className="overflow-hidden rounded-xl border border-slate-100">
              <Table
                headers={['Title', 'OEM', 'Product', 'Application', 'Vendor', 'Status', 'Date', 'Actions']}
                emptyMessage="No recommendations found"
              >
                {recs.map((r) => (
                  <tr key={r.id} className="hover:bg-slate-50 transition-colors border-b border-slate-100 last:border-0">
                    <td className="px-4 py-3 font-medium text-slate-900 max-w-[140px] truncate">
                      {r.title}
                    </td>
                    <td className="px-4 py-3 text-slate-500">{r.oemName || '—'}</td>
                    <td className="px-4 py-3 text-slate-500">{r.productName || '—'}</td>
                    <td className="px-4 py-3 text-slate-500">{r.applicationName || '—'}</td>
                    <td className="px-4 py-3 text-slate-500">{r.vendorName || '—'}</td>
                    <td className="px-4 py-3"><Badge status={r.status} /></td>
                    <td className="px-4 py-3 text-slate-400 text-xs tabular-nums">
                      {formatDate(r.uploadDate)}
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex gap-2">
                        {/* CHANGED: Combined Start Review + Assign into ONE button */}
                        {(r.status === 'DEPARTMENT_ASSIGNED' || r.status === 'UNDER_REVIEW') && (
                          <button
                            onClick={() => handleAssignFlow(r)}
                            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium
                                       bg-brand-600 text-white hover:bg-brand-700 transition-colors shadow-sm"
                          >
                            <FiUserCheck size={13} />
                            Assign Banker
                          </button>
                        )}
                        {r.status === 'IMPLEMENTED' && (
                          <button
                            onClick={() => handleReviewImplementation(r.id)}
                            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium
                                       bg-emerald-600 text-white hover:bg-emerald-700 transition-colors shadow-sm"
                          >
                            <FiClipboard size={13} />
                            Review
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </Table>
            </div>
          )}
        </div>
      </div>

      <Modal
        isOpen={!!assignModal}
        onClose={() => setAssignModal(null)}
        title="Assign to Banker"
      >
        <p className="text-sm text-slate-500 mb-4">
          Recommendation: <strong className="text-slate-900">{assignModal?.title}</strong>
        </p>
        <div className="space-y-4">
          {bankers.length === 0 ? (
            <p className="text-sm text-red-600 bg-red-50 border border-red-100 rounded-xl p-3">
              No bankers available in your department. Ask Admin to assign bankers first.
            </p>
          ) : (
            <select
              className="w-full text-sm rounded-lg border border-slate-200 bg-white px-3 py-2
                         text-slate-700 focus:outline-none focus:ring-2 focus:ring-brand-500 focus:border-brand-500"
              value={selectedBanker}
              onChange={(e) => setSelectedBanker(e.target.value)}
            >
              <option value="">Select Banker</option>
              {bankers.map((b) => (
                <option key={b.id} value={b.id}>{b.username}</option>
              ))}
            </select>
          )}
          <div className="flex gap-2 justify-end pt-2 border-t border-slate-100">
            <button
              onClick={() => setAssignModal(null)}
              className="px-4 py-2 rounded-lg text-sm font-medium text-slate-600 bg-slate-100 hover:bg-slate-200 transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleAssignBanker}
              disabled={!selectedBanker || bankers.length === 0}
              className="px-4 py-2 rounded-lg text-sm font-medium text-white bg-brand-600 hover:bg-brand-700
                         disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              Assign
            </button>
          </div>
        </div>
      </Modal>

    </DashboardLayout>
  );
};

export default DHRecommendations;