import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

export default function Pacientes() {
  const [pacientes, setPacientes] = useState([]);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);

  // Formulario nuevo paciente
  const [form, setForm] = useState({
    rut: '', nombre: '', email: '',
    fechaNac: '', fono: '', genero: 'M',
  });
  const [enviando, setEnviando] = useState(false);
  const [exito, setExito]       = useState(false);

  const cargarPacientes = () => {
    setLoading(true);
    api.get('/pacientes')
      .then(r => setPacientes(r.data))
      .catch(() => setError('No se pudo conectar con el servidor.'))
      .finally(() => setLoading(false));
  };

  useEffect(() => { cargarPacientes(); }, []);

  const handleSubmit = async e => {
    e.preventDefault();
    setEnviando(true);
    try {
      await api.post('/pacientes/registrar', {
        ...form,
        genero: form.genero.charCodeAt(0), // char en Java
      });
      setExito(true);
      setForm({ rut:'', nombre:'', email:'', fechaNac:'', fono:'', genero:'M' });
      cargarPacientes();
      setTimeout(() => setExito(false), 3000);
    } catch {
      alert('Error al registrar paciente.');
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="flex flex-col gap-8">
      <h1 className="text-2xl font-bold text-gray-800">Pacientes</h1>

      {/* Formulario */}
      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-4">
          Registrar nuevo paciente
        </h2>
        {exito && (
          <div className="mb-4 p-3 bg-green-100 text-green-700 rounded-lg text-sm">
            ✅ Paciente registrado correctamente.
          </div>
        )}
        <form onSubmit={handleSubmit}
          className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {[
            { name:'rut',     label:'RUT',        placeholder:'12345678-9' },
            { name:'nombre',  label:'Nombre',      placeholder:'Juan Pérez' },
            { name:'email',   label:'Email',       placeholder:'juan@mail.com', type:'email' },
            { name:'fechaNac',label:'Fecha Nac.',  placeholder:'', type:'date' },
            { name:'fono',    label:'Teléfono',    placeholder:'+56912345678' },
          ].map(({ name, label, placeholder, type='text' }) => (
            <div key={name} className="flex flex-col gap-1">
              <label className="text-sm font-medium text-gray-600">{label}</label>
              <input
                type={type}
                value={form[name]}
                onChange={e => setForm({ ...form, [name]: e.target.value })}
                placeholder={placeholder}
                required
                className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                  focus:outline-none focus:ring-2 focus:ring-medical-main"
              />
            </div>
          ))}
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-600">Género</label>
            <select
              value={form.genero}
              onChange={e => setForm({ ...form, genero: e.target.value })}
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main"
            >
              <option value="M">Masculino</option>
              <option value="F">Femenino</option>
            </select>
          </div>
          <div className="sm:col-span-2 lg:col-span-3">
            <button
              type="submit"
              disabled={enviando}
              className="bg-medical-main text-white px-6 py-2 rounded-lg
                font-semibold hover:bg-medical-dark transition-colors disabled:opacity-50"
            >
              {enviando ? 'Guardando...' : 'Registrar Paciente'}
            </button>
          </div>
        </form>
      </div>

      {/* Tabla */}
      <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
        <div className="p-6 border-b border-gray-100">
          <h2 className="text-lg font-semibold text-gray-700">Lista de pacientes</h2>
        </div>
        {loading ? (
          <p className="p-6 text-gray-400">Cargando...</p>
        ) : error ? (
          <p className="p-6 text-red-500">{error}</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-50 text-gray-500 uppercase text-xs">
                <tr>
                  {['ID','RUT','Nombre','Email','Teléfono','Acciones'].map(h => (
                    <th key={h} className="px-4 py-3 text-left font-medium">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {pacientes.map(p => (
                  <tr key={p.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-4 py-3 text-gray-500">{p.id}</td>
                    <td className="px-4 py-3 font-mono">{p.rut}</td>
                    <td className="px-4 py-3 font-medium text-gray-800">{p.nombre}</td>
                    <td className="px-4 py-3 text-gray-600">{p.email}</td>
                    <td className="px-4 py-3 text-gray-600">{p.fono ?? '—'}</td>
                    <td className="px-4 py-3">
                      <Link
                        to={`/pacientes/${p.id}`}
                        className="text-medical-main hover:underline font-medium"
                      >
                        Ver detalle
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}