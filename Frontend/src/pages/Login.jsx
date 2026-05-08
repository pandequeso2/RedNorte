import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { login, usuario } = useAuth();
  const navigate = useNavigate();
  const [params] = useSearchParams();
  const rolSugerido = params.get('rol') ?? 'paciente';

  const [rut, setRut]           = useState('');
  const [password, setPassword] = useState('');
  const [error, setError]       = useState('');
  const [cargando, setCargando] = useState(false);

  // Si ya está logueado, redirigir
  useEffect(() => {
    if (usuario) {
      navigate(usuario.rol === 'doctor' ? '/dashboard' : '/portal-paciente', { replace: true });
    }
  }, [usuario, navigate]);

  const handleSubmit = async e => {
    e.preventDefault();
    setCargando(true);
    setError('');
    await new Promise(r => setTimeout(r, 500));

    const ok = login(rut.trim(), password);
    if (!ok) {
      setError('RUT o contraseña incorrectos.');
      setCargando(false);
    }
    // el useEffect de arriba maneja la redirección cuando usuario cambia
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-medical-main to-medical-dark
      flex items-center justify-center px-4">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-8">
        <div className="text-center mb-6">
          <div className="w-14 h-14 bg-medical-main rounded-xl flex items-center
            justify-center mx-auto mb-3">
            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor"
              viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
            </svg>
          </div>
          <h1 className="text-2xl font-bold text-gray-800">Iniciar Sesión</h1>
          <p className="text-gray-500 text-sm mt-1">
            {rolSugerido === 'doctor' ? 'Portal Médico · RedNorte' : 'Portal Paciente · RedNorte'}
          </p>
        </div>

        <div className="bg-blue-50 border border-blue-200 rounded-lg p-3 mb-5 text-xs text-blue-700">
          <strong>Credenciales de prueba</strong> (contraseña: <code>1234</code>)<br/>
          {rolSugerido === 'doctor'
            ? <span>Médico: <code>DOCTOR-001</code> o <code>DOCTOR-002</code></span>
            : <span>Paciente: <code>12345678-9</code> o <code>98765432-1</code></span>
          }
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-600">RUT</label>
            <input type="text" value={rut}
              onChange={e => setRut(e.target.value)}
              placeholder={rolSugerido === 'doctor' ? 'DOCTOR-001' : '12345678-9'}
              required
              className="border border-gray-300 rounded-lg px-4 py-2.5 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main" />
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-gray-600">Contraseña</label>
            <input type="password" value={password}
              onChange={e => setPassword(e.target.value)}
              placeholder="••••••" required
              className="border border-gray-300 rounded-lg px-4 py-2.5 text-sm
                focus:outline-none focus:ring-2 focus:ring-medical-main" />
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg px-3 py-2
              text-red-600 text-sm">❌ {error}</div>
          )}

          <button type="submit" disabled={cargando}
            className="bg-medical-main text-white py-2.5 rounded-lg font-semibold
              hover:bg-medical-dark transition-colors disabled:opacity-50 mt-1">
            {cargando ? 'Verificando...' : 'Ingresar'}
          </button>
        </form>

        <div className="mt-5 text-center">
          <Link to="/" className="text-sm text-medical-main hover:underline">
            ← Volver al inicio
          </Link>
        </div>
      </div>
    </div>
  );
}