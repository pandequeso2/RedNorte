import { Link } from 'react-router-dom';

const cards = [
  {
    to: '/pacientes',
    title: 'Pacientes',
    desc: 'Registrar y buscar pacientes del sistema.',
    color: 'bg-blue-100 text-blue-700',
    icon: '👤',
  },
  {
    to: '/lista-espera',
    title: 'Lista de Espera',
    desc: 'Ver solicitudes pendientes y pacientes críticos.',
    color: 'bg-yellow-100 text-yellow-700',
    icon: '⏳',
  },
  {
    to: '/citas',
    title: 'Citas Médicas',
    desc: 'Gestionar y cancelar citas del sistema.',
    color: 'bg-red-100 text-red-700',
    icon: '📅',
  },
  {
    to: '/reasignacion',
    title: 'Reasignación',
    desc: 'Activar el motor de reasignación automática.',
    color: 'bg-green-100 text-green-700',
    icon: '🔄',
  },
  {
    to: '/notificaciones',
    title: 'Notificaciones',
    desc: 'Consultar mensajes pendientes por paciente.',
    color: 'bg-purple-100 text-purple-700',
    icon: '🔔',
  },
];

export default function Home() {
  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
        <p className="text-gray-500 mt-1">Sistema de gestión médica RedNorte</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {cards.map(({ to, title, desc, color, icon }) => (
          <Link
            key={to}
            to={to}
            className="bg-white rounded-xl shadow-sm border border-gray-200
              p-6 hover:shadow-md transition-shadow flex flex-col gap-3"
          >
            <div className={`w-12 h-12 rounded-lg flex items-center 
              justify-center text-2xl ${color}`}>
              {icon}
            </div>
            <h2 className="text-lg font-semibold text-gray-800">{title}</h2>
            <p className="text-gray-500 text-sm">{desc}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}