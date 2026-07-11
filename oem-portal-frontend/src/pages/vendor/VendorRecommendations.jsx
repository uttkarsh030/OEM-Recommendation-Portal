import { useState, useEffect } from 'react';
import DashboardLayout from '../../components/layouts/DashboardLayout';
import Table from '../../components/common/Table';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import api from '../../api/axios';
import { formatDate } from '../../utils/helpers';
import { FiPlus, FiEdit2, FiTrash2 } from 'react-icons/fi';

const empty = {
  title: '', description: '', oemName: '',
  productName: '', applicationName: '', version: '', releaseDate: ''
};

const VendorRecommendations = () => {
  const [recs, setRecs]         = useState([]);
  const [loading, setLoading]   = useState(true);
  const [modal, setModal]       = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [form, setForm]         = useState(empty);
  const [saving, setSaving]     = useState(false);
  const [error, setError]       = useState('');

  const fetchRecs = () => {
    api.get('/vendor/recommendations')
      .then(res => setRecs(res.data.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchRecs(); }, []);

  const openAdd = () => {
    setEditItem(null); setForm(empty);
    setError(''); setModal(true);
  };

  const openEdit = (rec) => {
    setEditItem(rec);
    setForm({
      title: rec.title, description: rec.description,
      oemName: rec.oemName, productName: rec.productName,
      applicationName: rec.applicationName,
      version: rec.version, releaseDate: rec.releaseDate,
    });
    setError(''); setModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true); setError('');
    try {
      if (editItem) await api.put(`/vendor/recommendations/${editItem.id}`, form);
      else          await api.post('/vendor/recommendations', form);
      setModal(false);
      fetchRecs();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this recommendation?')) return;
    await api.delete(`/vendor/recommendations/${id}`);
    fetchRecs();
  };

  const fields = [
    { key: 'title',           label: 'Title',            required: true },
    { key: 'oemName',         label: 'OEM Name',         required: true },
    { key: 'productName',     label: 'Product Name',     required: true },
    { key: 'applicationName', label: 'Application Name', required: true },
    { key: 'version',         label: 'Version',          required: false },
    { key: 'releaseDate',     label: 'Release Date',     required: false },
  ];

  return (
    <DashboardLayout title="My Recommendations" subtitle="Manage your submissions">
      <div className="space-y-5">
        <div className="flex justify-end">
          <button onClick={openAdd} className="btn-primary">
            <FiPlus size={16} /> Add Recommendation
          </button>
        </div>

        <div className="card">
          {loading ? <LoadingSpinner /> : (
            <Table
              headers={['Title', 'OEM', 'Product', 'Application', 'Status', 'Date', 'Actions']}
              emptyMessage="No recommendations uploaded yet"
            >
              {recs.map((r) => (
                <tr key={r.id} className="hover:bg-slate-50 transition-colors">
                  <td className="table-cell">
                    <p className="font-medium text-slate-900 max-w-[150px] truncate">
                      {r.title}
                    </p>
                    {r.version && (
                      <p className="text-xs text-slate-400 mt-0.5">v{r.version}</p>
                    )}
                  </td>
                  <td className="table-cell text-slate-600">{r.oemName || '—'}</td>
                  <td className="table-cell text-slate-500">{r.productName || '—'}</td>
                  <td className="table-cell text-slate-500">{r.applicationName || '—'}</td>
                  <td className="table-cell"><Badge status={r.status} /></td>
                  <td className="table-cell text-slate-400 text-xs">
                    {formatDate(r.uploadDate)}
                  </td>
                  <td className="table-cell">
                    {r.status === 'UPLOADED' && (
                      <div className="flex gap-2">
                        <button
                          onClick={() => openEdit(r)}
                          className="btn-ghost text-xs py-1.5"
                        >
                          <FiEdit2 size={13} /> Edit
                        </button>
                        <button
                          onClick={() => handleDelete(r.id)}
                          className="btn-ghost text-xs py-1.5 text-red-500 
                                     hover:bg-red-50"
                        >
                          <FiTrash2 size={13} /> Delete
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </Table>
          )}
        </div>
      </div>

      <Modal
        isOpen={modal}
        onClose={() => setModal(false)}
        title={editItem ? 'Edit Recommendation' : 'New Recommendation'}
        size="lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <p className="text-sm text-red-600 bg-red-50 border border-red-200 
                          rounded-lg px-3 py-2">
              {error}
            </p>
          )}
          <div className="grid grid-cols-2 gap-4">
            {fields.map((f) => (
              <div key={f.key} className={f.key === 'title' ? 'col-span-2' : ''}>
                <label className="label">
                  {f.label} {f.required && <span className="text-red-400">*</span>}
                </label>
                <input
                  type="text" className="input"
                  value={form[f.key]}
                  onChange={(e) => setForm({ ...form, [f.key]: e.target.value })}
                  required={f.required}
                />
              </div>
            ))}
          </div>
          <div>
            <label className="label">Description</label>
            <textarea
              className="input resize-none" rows={3}
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
            />
          </div>
          <div className="flex gap-3 justify-end pt-2">
            <button type="button" onClick={() => setModal(false)} className="btn-secondary">
              Cancel
            </button>
            <button type="submit" disabled={saving} className="btn-primary">
              {saving ? 'Saving...' : editItem ? 'Update' : 'Submit'}
            </button>
          </div>
        </form>
      </Modal>

    </DashboardLayout>
  );
};

export default VendorRecommendations;