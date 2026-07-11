import { useEffect } from 'react';
import { FiX } from 'react-icons/fi';

const Modal = ({ isOpen, onClose, title, children, size = 'md' }) => {
  useEffect(() => {
    if (isOpen) document.body.style.overflow = 'hidden';
    else document.body.style.overflow = '';
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  if (!isOpen) return null;

  const sizes = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-xl',
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div
        className="absolute inset-0 bg-slate-900/50 backdrop-blur-sm"
        onClick={onClose}
      />
      <div className={`
        relative bg-white rounded-2xl shadow-modal
        w-full ${sizes[size]} z-10
        animate-in fade-in zoom-in-95 duration-200
      `}>
        <div className="flex items-center justify-between 
                        px-6 py-4 border-b border-slate-100">
          <h3 className="text-base font-semibold text-slate-900">
            {title}
          </h3>
          <button
            onClick={onClose}
            className="btn-ghost p-1.5 rounded-lg -mr-1.5"
          >
            <FiX size={18} />
          </button>
        </div>
        <div className="px-6 py-5">{children}</div>
      </div>
    </div>
  );
};

export default Modal;