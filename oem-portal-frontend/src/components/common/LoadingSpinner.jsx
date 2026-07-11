const LoadingSpinner = ({ fullPage = false, message }) => {
  if (fullPage) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="flex flex-col items-center gap-3">
          <div className="w-10 h-10 rounded-full animate-spin
                         border-[3px] border-slate-200 border-t-brand-600" />
          <p className="text-sm text-slate-400 font-medium">
            {message || 'Loading...'}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col items-center justify-center py-16 gap-3">
      <div className="w-8 h-8 rounded-full animate-spin
                     border-2 border-slate-200 border-t-brand-600" />
      {message && (
        <p className="text-xs text-slate-400">{message}</p>
      )}
    </div>
  );
};

export default LoadingSpinner;