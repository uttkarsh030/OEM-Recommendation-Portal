const Table = ({ headers, children, emptyMessage = 'No data found', loading }) => (
  <div className="overflow-x-auto rounded-xl border border-slate-100">
    <table className="w-full text-sm">
      <thead>
        <tr>
          {headers.map((h, i) => (
            <th key={i} className="table-head first:rounded-tl-xl last:rounded-tr-xl">
              {h}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {!loading && !children && (
          <tr>
            <td colSpan={headers.length}
                className="text-center py-16 text-slate-400 text-sm">
              <div className="flex flex-col items-center gap-2">
                <span className="text-3xl">📭</span>
                {emptyMessage}
              </div>
            </td>
          </tr>
        )}
        {children}
      </tbody>
    </table>
  </div>
);

export default Table;