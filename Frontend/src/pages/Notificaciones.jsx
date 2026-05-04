import { useState } from 'react';
import api from '../api/axios';

export default function Notificaciones() {
  const [pacienteId, setPacienteId]   = useState('');
  const [notificaciones, setNotificaciones] = useState([]);
  const [loading, setLoading]         = useState(false);
  const [buscado, setBuscado]         = useState(false);

  const buscar = async () => {
    if (!pacienteId) return;
    setLoading(true);
    setBuscado(false);
    try {
      const r = await api.get(`/notificaciones/paciente/${pacienteId}/pendientes`);
      setNotificaciones(r.data);
      setBuscado(true);
    } catch {
      setNotificaciones([]);
      setBuscado(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col gap-6 max-w-2xl">
      <h1 className="text-2xl font-bold text-gray-800">Notificaciones</h1>

      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-4">
          Mensajes pendientes por paciente
        </h2>
        <div className="flex gap-3">
          <input
            type="number"
            placeholder="ID del paciente"
            value={pacienteId}
            onChange={e => setPacienteId(e.target.value)}
            className="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm
              focus:outline-none focus:ring-2 focus:ring-medical-main"
          />
          <button
            onClick={buscar}
            disabled={loading || !pacienteId}
            className="bg-medical-main text-white px-5 py-2 rounded-lg text-sm
              font-semibold hover:bg-medical-dark transition-colors disabled:opacity-50"
          >
            {loading ? 'Buscando...' : 'Buscar'}
          </button>
        </div>
      </div>

      {buscado && (
        notificaciones.length === 0 ? (
          <div className="bg-gray-50 rounded-xl border border-gray-200 p-6
            text-center text-gray-400">
            Sin notificaciones pendientes para este paciente.
          </div>
        ) : (
          <div className="flex flex-col gap-3">
            <p className="text-sm text-gray-500">
              {notificaciones.length} mensaje(s) no leído(s)
            </p>
            {notificaciones.map(n => (
              <div key={n.id}
                className="bg-white rounded-xl border border-gray-200
                  shadow-sm p-4 flex flex-col gap-1">
                <div className="flex justify-between items-start">
                  <span className="text-sm font-medium text-gray-800">
                    🔔 {n.mensaje}
                  </span>
                  <span className="text-xs text-gray-400 whitespace-nowrap ml-4">
                    {n.fechaEnvio?.replace('T', ' ').slice(0, 16)}
                  </span>
                </div>
                <span className="text-xs bg-yellow-100 text-yellow-700
                  px-2 py-0.5 rounded-full w-fit">
                  No leída
                </span>
              </div>
            ))}
          </div>
        )
      )}
    </div>
  );
}