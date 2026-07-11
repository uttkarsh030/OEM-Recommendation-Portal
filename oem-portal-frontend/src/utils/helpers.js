export const getStatusBadgeClass = (status) => {
  const map = {
    UPLOADED:            'badge-gray',
    DEPARTMENT_ASSIGNED: 'badge-blue',
    UNDER_REVIEW:        'badge-yellow',
    ASSIGNED:            'badge-blue',
    IN_PROGRESS:         'badge-orange',
    IMPLEMENTED:         'badge-purple',
    REVIEWED:            'badge-indigo',
    VERIFIED:            'badge-green',
    PENDING:             'badge-yellow',
    ACTIVE:              'badge-green',
    INACTIVE:            'badge-red',
  };
  return map[status] || 'badge-gray';
};

export const formatStatus = (status) => {
  if (!status) return '—';
  return status.replace(/_/g, ' ');
};

export const formatDate = (dateStr) => {
  if (!dateStr) return '—';
  return new Date(dateStr).toLocaleDateString('en-IN', {
    day: '2-digit', month: 'short', year: 'numeric',
  });
};

export const getInitials = (name) => {
  if (!name) return 'U';
  return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
};