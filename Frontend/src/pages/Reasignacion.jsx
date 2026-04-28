import { useState } from 'react';
import api from '../api/axios';

export default function Reasignacion() {
  const [citaId, setCitaId]     = useState('');
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading]     = useState(false);
  const [error, setError]         = useState(null);

  const ejecutar = async () => {
    if (!citaId) return;
    setLoading(true);
    setResultado(null);
    setError(null);
    try {
      const r = await api.post(`/reasignacion/procesar-cancelacion/${citaId}`);
      setResultado(r.data);
    } catch (e) {
      setError(e.response?.data ?? 'Error en el proceso de reasignación.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col gap-6 max-w-lg">
      <h1 className="text-2xl font-bold text-gray-800">Motor de Reasignación</h1>

      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-2">
          Activar reasignación manual
        </h2>
        <p className="text-sm text-gray-500 mb-4">
          Ejecuta el motor de reasignación para una cita cancelada específica,
          buscando el paciente más prioritario en lista de espera.
        </p>

        <div className="flex gap-3">
          <input
            type="number"
            placeholder="ID de cita cancelada"
            value={citaId}
            onChange={e => setCitaId(e.target.value)}
            className="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm
              focus:outline-none focus:ring-2 focus:ring-medical-main"
          />
          <button
            onClick={ejecutar}
            disabled={loading || !citaId}
            className="bg-medical-main text-white px-5 py-2 rounded-lg text-sm
              font-semibold hover:bg-medical-dark transition-colors disabled:opacity-50"
          >
            {loading ? 'Procesando...' : 'Ejecutar'}
          </button>
        </div>

        {resultado && (
          <div className="mt-4 p-4 bg-green-50 border border-green-200
            rounded-lg text-green-700 text-sm">
            ✅ {resultado}
          </div>
        )}
        {error && (
          <div className="mt-4 p-4 bg-red-50 border border-red-200
            rounded-lg text-red-600 text-sm">
            ❌ {error}
          </div>
        )}
      </div>
    </div>
  );
}