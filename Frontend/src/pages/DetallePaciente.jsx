import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/axios';

export default function DetallePaciente() {
  const { id } = useParams();
  const [paciente, setPaciente] = useState(null);
  const [loading, setLoading]   = useState(true);
  const [form, setForm]         = useState({ email: '', fono: '' });
  const [guardando, setGuardando] = useState(false);
  const [exito, setExito]         = useState(false);

  useEffect(() => {
    api.get(`/pacientes/${id}`)
      .then(r => {
        setPaciente(r.data);
        setForm({ email: r.data.email, fono: r.data.fono ?? '' });
      })
      .finally(() => setLoading(false));
  }, [id]);

  const handleUpdate = async e => {
    e.preventDefault();
    setGuardando(true);
    try {
      const r = await api.put(`/pacientes/${id}/contacto`, form);
      setPaciente(r.data);
      setExito(true);
      setTimeout(() => setExito(false), 3000);
    } catch {
      alert('Error al actualizar contacto.');
    } finally {
      setGuardando(false);
    }
  };

  if (loading) return <p className="text-gray-400 p-6">Cargando...</p>;
  if (!paciente) return <p className="text-red-500 p-6">Paciente no encontrado.</p>;

  const campos = [
    { label: 'ID',        valor: paciente.id },
    { label: 'RUT',       valor: paciente.rut },
    { label: 'Nombre',    valor: paciente.nombre },
    { label: 'Género',    valor: paciente.genero === 77 ? 'Masculino' : 'Femenino' },
    { label: 'Fecha Nac.',valor: paciente.fechaNac ?? '—' },
  ];

  return (
    <div className="flex flex-col gap-6 max-w-2xl">
      <div className="flex items-center gap-3">
        <Link to="/pacientes"
          className="text-medical-main hover:underline text-sm">
          ← Volver
        </Link>
        <h1 className="text-2xl font-bold text-gray-800">{paciente.nombre}</h1>
      </div>

      {/* Info */}
      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-4">Datos personales</h2>
        <dl className="grid grid-cols-2 gap-4">
          {campos.map(({ label, valor }) => (
            <div key={label}>
              <dt className="text-xs text-gray-400 uppercase font-medium">{label}</dt>
              <dd className="text-gray-800 font-medium mt-1">{valor}</dd>
            </div>
          ))}
        </dl>
      </div>

      {/* Editar contacto */}
      <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-4">
          Actualizar contacto
        </h2>
        {exito && (
          <div className="mb-4 p-3 bg-green-100 text-green-700 rounded-lg text-sm">
            ✅ Contacto actualizado.
          </div>
        )}
        <form onSubmit={handleUpdate} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-600">Email</label>
            <input
              type="email"
              value={form.email}
              onChange={e => setForm({ ...form, email: e.target.value })}
              required
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main"
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-600">Teléfono</label>
            <input
              type="text"
              value={form.fono}
              onChange={e => setForm({ ...form, fono: e.target.value })}
              required
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main"
            />
          </div>
          <button
            type="submit"
            disabled={guardando}
            className="bg-medical-main text-white px-6 py-2 rounded-lg
              font-semibold hover:bg-medical-dark transition-colors
              disabled:opacity-50 w-fit"
          >
            {guardando ? 'Guardando...' : 'Guardar cambios'}
          </button>
        </form>
      </div>
    </div>
  );
}