const colorMap = {
  blue:   { bg: 'bg-blue-50',    icon: 'text-blue-600',    border: 'border-blue-100' },
  green:  { bg: 'bg-emerald-50', icon: 'text-emerald-600', border: 'border-emerald-100' },
  yellow: { bg: 'bg-amber-50',   icon: 'text-amber-600',   border: 'border-amber-100' },
  purple: { bg: 'bg-purple-50',  icon: 'text-purple-600',  border: 'border-purple-100' },
  red:    { bg: 'bg-red-50',     icon: 'text-red-600',     border: 'border-red-100' },
  indigo: { bg: 'bg-indigo-50',  icon: 'text-indigo-600',  border: 'border-indigo-100' },
  orange: { bg: 'bg-orange-50',  icon: 'text-orange-600',  border: 'border-orange-100' },
};

const StatCard = ({ title, value, icon, color = 'blue', trend }) => {
  const c = colorMap[color] || colorMap.blue;

  return (
    <div className="card p-5 flex items-start gap-4 group
                    hover:shadow-[0_4px_12px_0_rgb(0_0_0/0.09)]
                    hover:-translate-y-px transition-all duration-200">

      <div className={`${c.bg} ${c.border} border p-3 rounded-xl shrink-0
                       transition-transform duration-200 group-hover:scale-105`}>
        <span className={`${c.icon} text-xl block`}>{icon}</span>
      </div>

      <div className="flex-1 min-w-0">
        <p className="text-xs font-semibold text-slate-400
                      uppercase tracking-wide truncate">
          {title}
        </p>
        <p className="text-2xl font-bold text-slate-900 mt-1 leading-none tabular-nums">
          {value ?? 0}
        </p>
        {trend && (
          <p className="text-xs text-slate-400 mt-1.5 leading-none">{trend}</p>
        )}
      </div>
    </div>
  );
};

export default StatCard;