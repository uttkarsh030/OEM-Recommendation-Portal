import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { formatDate } from '../../utils/helpers';
import { FiSend, FiCheckCircle } from 'react-icons/fi';

const Recommendations = () => {
  const [recs, setRecs]               = useState([]);
  const [loading, setLoading]         = useState(true);
  const [tab, setTab]                 = useState('all');
  const [assignModal, setAssignModal] = useState(null);
  const [departments, setDepartments] = useState([]);
  const [selectedDept, setSelectedDept] = useState('');
  const [saving, setSaving]           = useState(false);

  const tabs = [
    { key: 'all',      label: 'All',               url: '/admin/recommendations' },
    { key: 'uploaded', label: 'Pending Assignment', url: '/admin/recommendations/uploaded' },
    { key: 'reviewed', label: 'Awaiting Verify',    url: '/admin/recommendations/reviewed' },
  ];

  const fetchRecs = () => {
    setLoading(true);
    const url = tabs.find(t => t.key === tab)?.url;
    api.get(url)
      .then(res => setRecs(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchRecs(); }, [tab]);
  useEffect(() => {
    api.get('/admin/departments')
      .then(res => setDepartments(res.data.data || []));
  }, []);

  const handleAssign = async () => {
    if (!selectedDept) return;
    setSaving(true);
    try {
      await api.post('/admin/recommendations/assign', {
        recommendationId: assignModal.id,
        departmentId:     Number(selectedDept),
      });
      setAssignModal(null);
      setSelectedDept('');
      fetchRecs();
    } finally {
      setSaving(false);
    }
  };

  const handleVerify = async (id) => {
    await api.put(`/admin/recommendations/${id}/verify`);
    fetchRecs();
  };

  return (
    <DashboardLayout title="Recommendations" subtitle="Manage OEM recommendations">
      <div className="space-y-5">

        <div className="flex gap-1 bg-slate-100 rounded-lg p-1 w-fit">
          {tabs.map((t) => (
            <button
              key={t.key}
              onClick={() => setTab(t.key)}
              className={`px-4 py-1.5 rounded-md text-sm font-medium 
                          transition-all duration-150
                          ${tab === t.key
                            ? 'bg-white text-slate-900 shadow-sm'
                            : 'text-slate-500 hover:text-slate-700'
                          }`}
            >
              {t.label}
            </button>
          ))}
        </div>

        <div className="card">
          {loading ? <LoadingSpinner /> : (
            <Table
              headers={['Title', 'OEM', 'Vendor', 'Department', 'Status', 'Date', 'Actions']}
              emptyMessage="No recommendations found"
            >
              {recs.map((r) => (
                <tr key={r.id} className="hover:bg-slate-50 transition-colors">
                  <td className="table-cell">
                    <p className="font-medium text-slate-900 max-w-[160px] truncate">
                      {r.title}
                    </p>
                    <p className="text-xs text-slate-400 mt-0.5">
                      #{r.id}
                    </p>
                  </td>
                  <td className="table-cell text-slate-600">{r.oemName || '—'}</td>
                  <td className="table-cell text-slate-500">{r.vendorName || '—'}</td>
                  <td className="table-cell">
                    {r.departmentName
                      ? <span className="badge-blue">{r.departmentName}</span>
                      : <span className="text-slate-400 text-xs">Unassigned</span>
                    }
                  </td>
                  <td className="table-cell"><Badge status={r.status} /></td>
                  <td className="table-cell text-slate-400 text-xs">
                    {formatDate(r.uploadDate)}
                  </td>
                  <td className="table-cell">
                    {r.status === 'UPLOADED' && (
                      <button
                        onClick={() => setAssignModal(r)}
                        className="btn-primary text-xs py-1.5"
                      >
                        <FiSend size={12} /> Assign
                      </button>
                    )}
                    {r.status === 'REVIEWED' && (
                      <button
                        onClick={() => handleVerify(r.id)}
                        className="btn-success text-xs py-1.5"
                      >
                        <FiCheckCircle size={12} /> Verify
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </Table>
          )}
        </div>
      </div>

      <Modal
        isOpen={!!assignModal}
        onClose={() => setAssignModal(null)}
        title="Assign to Department"
      >
        <div className="space-y-4">
          <div className="bg-slate-50 rounded-lg p-3 border border-slate-200">
            <p className="text-xs text-slate-500">Recommendation</p>
            <p className="text-sm font-semibold text-slate-900 mt-0.5">
              {assignModal?.title}
            </p>
          </div>
          <div>
            <label className="label">Target Department</label>
            <select
              className="input"
              value={selectedDept}
              onChange={(e) => setSelectedDept(e.target.value)}
            >
              <option value="">Select department...</option>
              {departments
                .filter(d => d.active && d.departmentHeadName)
                .map((d) => (
                  <option key={d.id} value={d.id}>{d.name}</option>
                ))}
            </select>
          </div>
          <div className="flex gap-3 justify-end">
            <button onClick={() => setAssignModal(null)} className="btn-secondary">
              Cancel
            </button>
            <button
              onClick={handleAssign}
              disabled={!selectedDept || saving}
              className="btn-primary"
            >
              {saving ? 'Assigning...' : 'Assign'}
            </button>
          </div>
        </div>
      </Modal>

    </DashboardLayout>
  );
};

export default Recommendations;