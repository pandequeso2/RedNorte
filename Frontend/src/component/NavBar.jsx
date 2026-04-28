import { Link, useLocation } from 'react-router-dom';

const links = [
  { to: '/',               label: 'Dashboard' },
  { to: '/pacientes',      label: 'Pacientes' },
  { to: '/lista-espera',   label: 'Lista de Espera' },
  { to: '/citas',          label: 'Citas Médicas' },
  { to: '/reasignacion',   label: 'Reasignación' },
  { to: '/notificaciones', label: 'Notificaciones' },
];

export default function Navbar() {
  const { pathname } = useLocation();

  return (
    <nav className="bg-medical-main text-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex items-center gap-2">
            <svg className="w-8 h-8" fill="none" stroke="currentColor"
              viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
            </svg>
            <span className="font-bold text-xl tracking-wider">RedNorte</span>
          </div>

          <div className="hidden md:flex space-x-1">
            {links.map(({ to, label }) => (
              <Link
                key={to}
                to={to}
                className={`px-3 py-2 rounded-md font-medium transition-colors
                  ${pathname === to
                    ? 'bg-white text-medical-main'
                    : 'hover:bg-medical-dark'}`}
              >
                {label}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </nav>
  );
}