import { getStatusBadgeClass, formatStatus } from '../../utils/helpers';

const Badge = ({ status }) => (
  <span className={getStatusBadgeClass(status)}>
    {formatStatus(status)}
  </span>
);

export default Badge;