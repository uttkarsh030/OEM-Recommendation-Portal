import Sidebar from '../common/Sidebar';
import Navbar  from '../common/Navbar';

const DashboardLayout = ({ children, title, subtitle }) => (
  <div className="flex h-screen bg-slate-50 overflow-hidden">
    <Sidebar />
    <div className="flex flex-col flex-1 overflow-hidden min-w-0">
      <Navbar title={title} subtitle={subtitle} />
      <main className="flex-1 overflow-y-auto bg-slate-50">
        <div className="p-6 max-w-7xl mx-auto">
          {children}
        </div>
      </main>
    </div>
  </div>
);

export default DashboardLayout;