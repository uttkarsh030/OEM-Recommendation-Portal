import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { FiCheck, FiX, FiMapPin } from 'react-icons/fi';

const Bankers = () => {
  const [bankers, setBankers]         = useState([]);
  const [loading, setLoading]         = useState(true);
  const [tab, setTab]                 = useState('all');
  const [assignModal, setAssignModal] = useState(null);
  const [departments, setDepartments] = useState([]);
  const [selectedDept, setSelectedDept] = useState('');
  const [saving, setSaving]           = useState(false);

  const fetchBankers = () => {
    setLoading(true);
    const url = tab === 'pending' ? '/admin/bankers/pending' : '/admin/bankers';
    api.get(url)
      .then(res => setBankers(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchBankers(); }, [tab]);
  useEffect(() => {
    api.get('/admin/departments')
      .then(res => setDepartments(res.data.data || []));
  }, []);

  const handleApprove = async (id) => {
    await api.put(`/admin/bankers/${id}/approve`);
    fetchBankers();
  };

  const handleReject = async (id) => {
    if (!window.confirm('Reject this banker?')) return;
    await api.put(`/admin/bankers/${id}/reject`);
    fetchBankers();
  };

  const handleAssign = async () => {
    if (!selectedDept) return;
    setSaving(true);
    await api.post('/admin/bankers/assign-department', {
      bankerId: assignModal.id, departmentId: Number(selectedDept),
    });
    setSaving(false);
    setAssignModal(null);
    setSelectedDept('');
    fetchBankers();
  };

  const tabs = [
    { key: 'all',     label: 'All Bankers' },
    { key: 'pending', label: 'Pending Approval' },
  ];

  return (
    <DashboardLayout title="Bankers" subtitle="Manage banker registrations and assignments">
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
              headers={['Banker', 'Email', 'Status', 'Department', 'Actions']}
              emptyMessage="No bankers found"
            >
              {bankers.map((b) => (
                <tr key={b.id} className="hover:bg-slate-50 transition-colors">
                  <td className="table-cell font-semibold text-slate-900">
                    {b.username}
                  </td>
                  <td className="table-cell text-slate-500">{b.email}</td>
                  <td className="table-cell"><Badge status={b.status} /></td>
                  <td className="table-cell">
                    {b.departmentName ? (
                      <span className="badge-blue">{b.departmentName}</span>
                    ) : (
                      <span className="text-slate-400 text-xs">Unassigned</span>
                    )}
                  </td>
                  <td className="table-cell">
                    <div className="flex gap-2">
                      {b.status === 'PENDING' && (
                        <>
                          <button
                            onClick={() => handleApprove(b.id)}
                            className="btn-success text-xs py-1.5"
                          >
                            <FiCheck size={13} /> Approve
                          </button>
                          <button
                            onClick={() => handleReject(b.id)}
                            className="btn-danger text-xs py-1.5"
                          >
                            <FiX size={13} /> Reject
                          </button>
                        </>
                      )}
                      {b.status === 'ACTIVE' && !b.departmentName && (
                        <button
                          onClick={() => setAssignModal(b)}
                          className="btn-secondary text-xs py-1.5"
                        >
                          <FiMapPin size={13} /> Assign Dept
                        </button>
                      )}
                    </div>
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
        title={`Assign ${assignModal?.username} to Department`}
      >
        <div className="space-y-4">
          <div>
            <label className="label">Select Department</label>
            <select
              className="input"
              value={selectedDept}
              onChange={(e) => setSelectedDept(e.target.value)}
            >
              <option value="">Choose a department...</option>
              {departments.filter(d => d.active).map((d) => (
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

export default Bankers;