import { useEffect, useState } from 'react';
import api from '../api/axios';

const TIPOS = [
  'CONSULTA_MEDICA','PROCEDIMIENTO',
  'DIAGNOSTICO','INTERVENCION_QUIRURGICA','URGENCIA_EXTREMA',
];

const ESTADO_COLOR = {
  PENDIENTE: 'bg-yellow-100 text-yellow-700',
  ASIGNADO:  'bg-green-100 text-green-700',
  CANCELADO: 'bg-red-100 text-red-700',
};

export default function ListaEspera() {
  const [solicitudes, setSolicitudes] = useState([]);
  const [loading, setLoading]         = useState(true);
  const [tab, setTab]                 = useState('criticos'); // 'criticos' | 'buscar' | 'nueva'
  const [pacienteId, setPacienteId]   = useState('');
  const [form, setForm] = useState({
    pacienteId: '', especialidadId: '', tipoAtencion: 'CONSULTA_MEDICA',
    prioridad: 1, observaciones: '',
  });
  const [enviando, setEnviando] = useState(false);
  const [exito, setExito]       = useState(false);

  const cargarCriticos = () => {
    setLoading(true);
    api.get('/listaEspera/criticos')
      .then(r => setSolicitudes(r.data))
      .finally(() => setLoading(false));
  };

  useEffect(() => { if (tab === 'criticos') cargarCriticos(); }, [tab]);

  const buscarPorPaciente = () => {
    if (!pacienteId) return;
    setLoading(true);
    api.get(`/listaEspera/paciente/${pacienteId}`)
      .then(r => setSolicitudes(r.data))
      .finally(() => setLoading(false));
  };

  const handleRegistrar = async e => {
    e.preventDefault();
    setEnviando(true);
    try {
      await api.post('/listaEspera/registrar', form);
      setExito(true);
      setForm({
        pacienteId:'', especialidadId:'', tipoAtencion:'CONSULTA_MEDICA',
        prioridad:1, observaciones:'',
      });
      setTimeout(() => setExito(false), 3000);
    } catch {
      alert('Error al registrar solicitud.');
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="flex flex-col gap-6">
      <h1 className="text-2xl font-bold text-gray-800">Lista de Espera</h1>

      {/* Tabs */}
      <div className="flex gap-2 border-b border-gray-200">
        {[
          { key:'criticos', label:'Pacientes Críticos' },
          { key:'buscar',   label:'Buscar por Paciente' },
          { key:'nueva',    label:'Nueva Solicitud' },
        ].map(({ key, label }) => (
          <button key={key} onClick={() => setTab(key)}
            className={`pb-3 px-4 text-sm font-medium border-b-2 transition-colors
              ${tab === key
                ? 'border-medical-main text-medical-main'
                : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
            {label}
          </button>
        ))}
      </div>

      {/* Criticos */}
      {tab === 'criticos' && (
        <TablaeSolicitudes solicitudes={solicitudes} loading={loading} />
      )}

      {/* Buscar */}
      {tab === 'buscar' && (
        <div className="flex flex-col gap-4">
          <div className="flex gap-3">
            <input
              type="number"
              placeholder="ID del paciente"
              value={pacienteId}
              onChange={e => setPacienteId(e.target.value)}
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main"
            />
            <button onClick={buscarPorPaciente}
              className="bg-medical-main text-white px-5 py-2 rounded-lg
                text-sm font-semibold hover:bg-medical-dark transition-colors">
              Buscar
            </button>
          </div>
          <TablaeSolicitudes solicitudes={solicitudes} loading={loading} />
        </div>
      )}

      {/* Nueva */}
      {tab === 'nueva' && (
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6 max-w-xl">
          {exito && (
            <div className="mb-4 p-3 bg-green-100 text-green-700 rounded-lg text-sm">
              ✅ Solicitud registrada.
            </div>
          )}
          <form onSubmit={handleRegistrar} className="flex flex-col gap-4">
            {[
              { name:'pacienteId',   label:'ID Paciente',    type:'number' },
              { name:'especialidadId', label:'ID Especialidad', type:'number' },
            ].map(({ name, label, type }) => (
              <div key={name} className="flex flex-col gap-1">
                <label className="text-sm font-medium text-gray-600">{label}</label>
                <input type={type} value={form[name]} required
                  onChange={e => setForm({ ...form, [name]: e.target.value })}
                  className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                    focus:outline-none focus:ring-2 focus:ring-medical-main" />
              </div>
            ))}
            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">Tipo Atención</label>
              <select value={form.tipoAtencion}
                onChange={e => setForm({ ...form, tipoAtencion: e.target.value })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main">
                {TIPOS.map(t => <option key={t} value={t}>{t}</option>)}
              </select>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">
                Prioridad (1=Baja, 5=Extrema)
              </label>
              <input type="number" min="1" max="5" value={form.prioridad}
                onChange={e => setForm({ ...form, prioridad: Number(e.target.value) })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main" />
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">Observaciones</label>
              <textarea value={form.observaciones} rows={3}
                onChange={e => setForm({ ...form, observaciones: e.target.value })}
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main" />
            </div>
            <button type="submit" disabled={enviando}
              className="bg-medical-main text-white px-6 py-2 rounded-lg
                font-semibold hover:bg-medical-dark transition-colors disabled:opacity-50 w-fit">
              {enviando ? 'Guardando...' : 'Registrar'}
            </button>
          </form>
        </div>
      )}
    </div>
  );
}

function TablaeSolicitudes({ solicitudes, loading }) {
  const ESTADO_COLOR = {
    PENDIENTE: 'bg-yellow-100 text-yellow-700',
    ASIGNADO:  'bg-green-100 text-green-700',
    CANCELADO: 'bg-red-100 text-red-700',
  };

  if (loading) return <p className="text-gray-400">Cargando...</p>;
  if (!solicitudes.length) return <p className="text-gray-400">Sin resultados.</p>;

  return (
    <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-500 uppercase text-xs">
            <tr>
              {['ID','Paciente','Especialidad','Tipo','Prioridad','Estado','Ingreso'].map(h => (
                <th key={h} className="px-4 py-3 text-left font-medium">{h}</th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {solicitudes.map(s => (
              <tr key={s.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-gray-500">{s.id}</td>
                <td className="px-4 py-3">{s.pacienteId}</td>
                <td className="px-4 py-3">{s.especialidadId}</td>
                <td className="px-4 py-3 text-xs">{s.tipoAtencion}</td>
                <td className="px-4 py-3 font-bold text-center">{s.prioridad}</td>
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
    </div>
  );
}