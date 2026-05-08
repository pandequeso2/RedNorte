import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function PortalPaciente() {
  const { usuario, logout } = useAuth();
  const [solicitudes, setSolicitudes] = useState([]);
  const [notificaciones, setNotificaciones] = useState([]);
  const [loadingSol, setLoadingSol]   = useState(true);
  const [loadingNoti, setLoadingNoti] = useState(true);

  // Formulario nueva solicitud
  const [form, setForm] = useState({
    especialidadId: '', tipoAtencion: 'CONSULTA_MEDICA', prioridad: 1, observaciones: '',
  });
  const [enviando, setEnviando] = useState(false);
  const [exito, setExito]       = useState(false);

  const TIPOS = ['CONSULTA_MEDICA','PROCEDIMIENTO','DIAGNOSTICO','INTERVENCION_QUIRURGICA','URGENCIA_EXTREMA'];

  useEffect(() => {
    if (!usuario?.pacienteId) return;

    api.get(`/listaEspera/paciente/${usuario.pacienteId}`)
      .then(r => setSolicitudes(r.data))
      .finally(() => setLoadingSol(false));

    api.get(`/notificaciones/paciente/${usuario.pacienteId}/pendientes`)
      .then(r => setNotificaciones(r.data))
      .finally(() => setLoadingNoti(false));
  }, [usuario]);

  const handleSolicitud = async e => {
    e.preventDefault();
    setEnviando(true);
    try {
      await api.post('/listaEspera/registrar', {
        ...form,
        pacienteId: usuario.pacienteId,
        estado: 'PENDIENTE',
      });
      setExito(true);
      setForm({ especialidadId:'', tipoAtencion:'CONSULTA_MEDICA', prioridad:1, observaciones:'' });
      // Recargar solicitudes
      const r = await api.get(`/listaEspera/paciente/${usuario.pacienteId}`);
      setSolicitudes(r.data);
      setTimeout(() => setExito(false), 3000);
    } catch {
      alert('Error al enviar solicitud.');
    } finally {
      setEnviando(false);
    }
  };

  const ESTADO_COLOR = {
    PENDIENTE: 'bg-yellow-100 text-yellow-700',
    ASIGNADO:  'bg-green-100 text-green-700',
    CANCELADO: 'bg-red-100 text-red-700',
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar del paciente */}
      <nav className="bg-medical-main text-white shadow-md">
        <div className="max-w-4xl mx-auto px-4 h-16 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517
                   l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1
                   1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415
                   3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0
                   009 10.172V5L8 4z" />
            </svg>
            <span className="font-bold text-lg">RedNorte · Portal Paciente</span>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-white/80">👤 {usuario?.nombre}</span>
            <button onClick={logout}
              className="bg-white text-medical-main px-3 py-1.5 rounded-lg
                text-sm font-semibold hover:bg-medical-light transition-colors">
              Cerrar sesión
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-4xl mx-auto px-4 py-8 flex flex-col gap-8">
        {/* Bienvenida */}
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
          <h1 className="text-2xl font-bold text-gray-800 mb-1">
            Bienvenido/a, {usuario?.nombre}
          </h1>
          <p className="text-gray-500 text-sm">
            Aquí puedes ver el estado de tus solicitudes médicas y tus notificaciones.
          </p>
        </div>

        {/* Notificaciones no leídas */}
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-4">
            🔔 Notificaciones pendientes
          </h2>
          {loadingNoti ? (
            <p className="text-gray-400 text-sm">Cargando...</p>
          ) : notificaciones.length === 0 ? (
            <p className="text-gray-400 text-sm">No tienes notificaciones pendientes.</p>
          ) : (
            <div className="flex flex-col gap-3">
              {notificaciones.map(n => (
                <div key={n.id}
                  className="bg-yellow-50 border border-yellow-200 rounded-lg p-3
                    flex justify-between items-start gap-3">
                  <p className="text-sm text-gray-800">{n.mensaje}</p>
                  <span className="text-xs text-gray-400 whitespace-nowrap">
                    {n.fechaEnvio?.replace('T',' ').slice(0,16)}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Mis solicitudes */}
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
          <div className="p-6 border-b border-gray-100">
            <h2 className="text-lg font-semibold text-gray-700">📋 Mis solicitudes en lista de espera</h2>
          </div>
          {loadingSol ? (
            <p className="p-6 text-gray-400 text-sm">Cargando...</p>
          ) : solicitudes.length === 0 ? (
            <p className="p-6 text-gray-400 text-sm">No tienes solicitudes registradas.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead className="bg-gray-50 text-gray-500 uppercase text-xs">
                  <tr>
                    {['Especialidad','Tipo','Prioridad','Estado','Fecha Ingreso'].map(h => (
                      <th key={h} className="px-4 py-3 text-left font-medium">{h}</th>
                    ))}
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {solicitudes.map(s => (
                    <tr key={s.id} className="hover:bg-gray-50">
                      <td className="px-4 py-3">Esp. {s.especialidadId}</td>
                      <td className="px-4 py-3 text-xs">{s.tipoAtencion?.replace(/_/g,' ')}</td>
                      <td className="px-4 py-3 text-center font-bold">{s.prioridad}</td>
                      <td className="px-4 py-3">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium
                          ${ESTADO_COLOR[s.estado] ?? 'bg-gray-100 text-gray-600'}`}>
                          {s.estado}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-gray-400 text-xs">
                        {s.fechaIngreso?.replace('T',' ').slice(0,16)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Nueva solicitud */}
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-4">
            ➕ Solicitar nueva atención médica
          </h2>
          {exito && (
            <div className="mb-4 p-3 bg-green-100 text-green-700 rounded-lg text-sm">
              ✅ Solicitud enviada correctamente. El equipo médico la revisará pronto.
            </div>
          )}
          <form onSubmit={handleSolicitud} className="flex flex-col gap-4 max-w-md">
            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">ID Especialidad</label>
              <input type="number" value={form.especialidadId} required
                placeholder="Ej: 1 (Cardiología), 2 (Traumatología)..."
                onChange={e => setForm({ ...form, especialidadId: e.target.value })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main" />
              <p className="text-xs text-gray-400">
                1=Cardiología · 2=Traumatología · 3=Neurología · 4=Pediatría · 5=Ginecología
              </p>
            </div>

            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">Tipo de atención</label>
              <select value={form.tipoAtencion}
                onChange={e => setForm({ ...form, tipoAtencion: e.target.value })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main">
                {TIPOS.map(t => (
                  <option key={t} value={t}>{t.replace(/_/g,' ')}</option>
                ))}
              </select>
            </div>

            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">
                Urgencia (1=Baja · 5=Extrema)
              </label>
              <input type="number" min="1" max="5" value={form.prioridad}
                onChange={e => setForm({ ...form, prioridad: Number(e.target.value) })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main" />
            </div>

            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">
                Descripción del motivo (opcional)
              </label>
              <textarea rows={3} value={form.observaciones}
                onChange={e => setForm({ ...form, observaciones: e.target.value })}
                placeholder="Describe brevemente tus síntomas o motivo de consulta..."
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main" />
            </div>

            <button type="submit" disabled={enviando}
              className="bg-medical-main text-white px-6 py-2.5 rounded-lg
                font-semibold hover:bg-medical-dark transition-colors
                disabled:opacity-50 w-fit">
              {enviando ? 'Enviando...' : 'Enviar solicitud'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}