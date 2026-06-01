import { Link } from 'react-router-dom';

export default function Welcome() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-medical-main to-medical-dark
      flex flex-col items-center justify-center px-4 text-white">

      {/* Logo / Ícono */}
      <div className="mb-6">
        <svg className="w-20 h-20 mx-auto" fill="none" stroke="currentColor"
          viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="1.5"
            d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517
               l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1
               1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415
               3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0
               009 10.172V5L8 4z" />
        </svg>
      </div>

      {/* Título */}
      <h1 className="text-4xl font-bold tracking-tight text-center mb-2">
        RedNorte
      </h1>
      <p className="text-medical-light text-lg text-center mb-2">
        Servicio Público de Salud
      </p>
      <p className="text-white/70 text-sm text-center max-w-md mb-10">
        Plataforma inteligente para la gestión de listas de espera hospitalarias
      </p>

      {/* Tarjetas de acceso */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-5 w-full max-w-xl">
        {/* Paciente */}
        <Link to="/login?rol=paciente"
          className="bg-white/10 backdrop-blur border border-white/20 rounded-2xl
            p-6 hover:bg-white/20 transition-all text-center group">
          <div className="text-4xl mb-3">🏥</div>
          <h2 className="text-xl font-semibold mb-1">Portal Paciente</h2>
          <p className="text-white/70 text-sm">
            Consulta tu estado de espera y tus notificaciones
          </p>
          <span className="mt-4 inline-block text-xs bg-white/20 px-3 py-1
            rounded-full group-hover:bg-white/30 transition-colors">
            Ingresar →
          </span>
        </Link>

        {/* Doctor */}
        <Link to="/login?rol=doctor"
          className="bg-white/10 backdrop-blur border border-white/20 rounded-2xl
            p-6 hover:bg-white/20 transition-all text-center group">
          <div className="text-4xl mb-3">👨‍⚕️</div>
          <h2 className="text-xl font-semibold mb-1">Portal Médico</h2>
          <p className="text-white/70 text-sm">
            Gestiona citas, listas de espera y reasignaciones
          </p>
          <span className="mt-4 inline-block text-xs bg-white/20 px-3 py-1
            rounded-full group-hover:bg-white/30 transition-colors">
            Ingresar →
          </span>
        </Link>
      </div>

      <p className="mt-10 text-white/40 text-xs">
        © 2025 RedNorte · Sistema de Gestión Hospitalaria
      </p>
    </div>
  );
}