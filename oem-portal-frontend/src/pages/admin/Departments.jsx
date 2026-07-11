import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { FiPlus, FiUserPlus, FiPower } from 'react-icons/fi';

const Departments = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading]         = useState(true);
  const [createModal, setCreateModal] = useState(false);
  const [headModal, setHeadModal]     = useState(null);
  const [form, setForm]               = useState({ name: '', description: '' });
  const [headForm, setHeadForm]       = useState({ username: '', email: '', password: '' });
  const [saving, setSaving]           = useState(false);
  const [error, setError]             = useState('');

  const loadDepartments = () => {
    api.get('/admin/departments')
      .then(res => setDepartments(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { loadDepartments(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await api.post('/admin/departments', form);
      setCreateModal(false);
      setForm({ name: '', description: '' });
      loadDepartments();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create department');
    } finally {
      setSaving(false);
    }
  };

  const handleAssignHead = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await api.post('/admin/departments/assign-head', {
        ...headForm, departmentId: headModal.id,
      });
      setHeadModal(null);
      setHeadForm({ username: '', email: '', password: '' });
      loadDepartments();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to assign head');
    } finally {
      setSaving(false);
    }
  };

  const handleToggleStatus = async (dept) => {
    const action = dept.active ? 'Disable' : 'Enable';
    if (!window.confirm(`${action} "${dept.name}"?`)) return;
    await api.put(`/admin/departments/${dept.id}/toggle-status`);
    loadDepartments();
  };

  return (
    <DashboardLayout title="Departments" subtitle="Manage organizational departments">
      <div className="space-y-5">

        {/* Header row */}
        <div className="flex items-center justify-between">
          <p className="text-sm font-medium text-slate-600">
            {departments.length} department{departments.length !== 1 ? 's' : ''} total
          </p>
          <button onClick={() => setCreateModal(true)} className="btn-primary">
            <FiPlus size={16} /> New Department
          </button>
        </div>

        {/* Table */}
        <div className="card overflow-hidden">
          {loading ? <LoadingSpinner /> : (
            <Table
              headers={['Department', 'Description', 'Head', 'Bankers', 'Status', 'Actions']}
              emptyMessage="No departments created yet"
            >
              {departments.map((d) => (
                <tr key={d.id} className="table-row">

                  {/* Department name */}
                  <td className="table-cell">
                    <span className="font-semibold text-slate-900 text-sm">
                      {d.name}
                    </span>
                  </td>

                  {/* Description */}
                  <td className="table-cell max-w-[200px]">
                    <span className="text-sm text-slate-600 truncate block">
                      {d.description || '—'}
                    </span>
                  </td>

                  {/* Head */}
                  <td className="table-cell">
                    {d.departmentHeadName ? (
                      <div>
                        <p className="text-sm font-semibold text-slate-900 leading-none">
                          {d.departmentHeadName}
                        </p>
                        <p className="text-xs text-slate-500 mt-1">
                          {d.departmentHeadEmail}
                        </p>
                      </div>
                    ) : (
                      <span className="badge-yellow">Not Assigned</span>
                    )}
                  </td>

                  {/* Bankers count */}
                  <td className="table-cell">
                    <span className="badge-blue">
                      {d.bankers?.length || 0} bankers
                    </span>
                  </td>

                  {/* Status */}
                  <td className="table-cell">
                    <span className={d.active ? 'badge-green' : 'badge-red'}>
                      {d.active ? 'Active' : 'Disabled'}
                    </span>
                  </td>

                  {/* Actions */}
                  <td className="table-cell">
                    <div className="flex items-center gap-2">
                      {!d.departmentHeadName && d.active && (
                        <button
                          onClick={() => { setHeadModal(d); setError(''); }}
                          className="btn-secondary text-xs py-1.5 px-3"
                        >
                          <FiUserPlus size={13} /> Assign Head
                        </button>
                      )}
                      <button
                        onClick={() => handleToggleStatus(d)}
                        className={`inline-flex items-center gap-1.5 text-xs font-medium
                                    px-3 py-1.5 rounded-lg transition-all duration-150
                                    ${d.active
                                      ? 'text-red-600 bg-red-50 hover:bg-red-100 border border-red-200'
                                      : 'text-emerald-600 bg-emerald-50 hover:bg-emerald-100 border border-emerald-200'
                                    }`}
                      >
                        <FiPower size={13} />
                        {d.active ? 'Disable' : 'Enable'}
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </Table>
          )}
        </div>
      </div>

      {/* Create Department Modal */}
      <Modal
        isOpen={createModal}
        onClose={() => { setCreateModal(false); setError(''); }}
        title="Create New Department"
      >
        <form onSubmit={handleCreate} className="space-y-4">
          {error && (
            <p className="text-sm text-red-700 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
              {error}
            </p>
          )}
          <div>
            <label className="label">Department Name</label>
            <input
              type="text"
              className="input"
              placeholder="e.g. IT Infrastructure"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="label">Description</label>
            <textarea
              className="input resize-none"
              rows={3}
              placeholder="What does this department handle?"
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
            />
          </div>
          <div className="flex gap-3 justify-end pt-2">
            <button type="button" onClick={() => setCreateModal(false)} className="btn-secondary">
              Cancel
            </button>
            <button type="submit" disabled={saving} className="btn-primary">
              {saving ? 'Creating...' : 'Create Department'}
            </button>
          </div>
        </form>
      </Modal>

      {/* Assign Head Modal */}
      <Modal
        isOpen={!!headModal}
        onClose={() => { setHeadModal(null); setError(''); }}
        title={`Assign Head — ${headModal?.name}`}
      >
        <form onSubmit={handleAssignHead} className="space-y-4">
          {error && (
            <p className="text-sm text-red-700 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
              {error}
            </p>
          )}
          {[
            { key: 'username', label: 'Username', type: 'text',     placeholder: 'department.head' },
            { key: 'email',    label: 'Email',    type: 'email',    placeholder: 'head@bank.com'   },
            { key: 'password', label: 'Password', type: 'password', placeholder: '••••••••'        },
          ].map(({ key, label, type, placeholder }) => (
            <div key={key}>
              <label className="label">{label}</label>
              <input
                type={type}
                className="input"
                placeholder={placeholder}
                value={headForm[key]}
                onChange={(e) => setHeadForm({ ...headForm, [key]: e.target.value })}
                required
              />
            </div>
          ))}
          <div className="flex gap-3 justify-end pt-2">
            <button type="button" onClick={() => setHeadModal(null)} className="btn-secondary">
              Cancel
            </button>
            <button type="submit" disabled={saving} className="btn-primary">
              {saving ? 'Assigning...' : 'Assign Head'}
            </button>
          </div>
        </form>
      </Modal>

    </DashboardLayout>
  );
};

export default Departments;