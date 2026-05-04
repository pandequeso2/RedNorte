import { useState } from 'react';
import api from '../api/axios';

export default function CitasMedicas() {
  const [citaId, setCitaId]     = useState('');
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading]     = useState(false);
  const [error, setError]         = useState(null);

  const cancelarCita = async () => {
    if (!citaId) return;
    setLoading(true);
    setResultado(null);
    setError(null);
    try {
      const r = await api.post(`/citas/${citaId}/cancelar`);
      setResultado(r.data);
    } catch (e) {
      setError(e.response?.data ?? 'Error al cancelar la cita.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col gap-6 max-w-lg">
      <h1 className="text-2xl font-bold text-gray-800">Citas Médicas</h1>

      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-2">Cancelar una cita</h2>
        <p className="text-sm text-gray-500 mb-4">
          Al cancelar, el sistema activará automáticamente el motor de reasignación
          para asignar la hora al paciente más prioritario en lista de espera.
        </p>

        <div className="flex gap-3">
          <input
            type="number"
            placeholder="ID de la cita"
            value={citaId}
            onChange={e => setCitaId(e.target.value)}
            className="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm
              focus:outline-none focus:ring-2 focus:ring-red-400"
          />
          <button
            onClick={cancelarCita}
            disabled={loading || !citaId}
            className="bg-red-500 text-white px-5 py-2 rounded-lg text-sm
              font-semibold hover:bg-red-600 transition-colors disabled:opacity-50"
          >
            {loading ? 'Procesando...' : 'Cancelar Cita'}
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

      <div className="bg-blue-50 border border-blue-200 rounded-xl p-4 text-sm text-blue-700">
        <strong>¿Cómo funciona?</strong> Al cancelar una cita, el backend publica
        un evento interno que busca automáticamente al candidato con mayor prioridad
        en la lista de espera para la misma especialidad.
      </div>
    </div>
  );
}