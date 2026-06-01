import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout         from './components/Layout';
import Welcome        from './pages/Welcome';
import Login          from './pages/Login';
import PortalPaciente from './pages/PortalPaciente';
import Home           from './pages/Home';
import Pacientes      from './pages/Pacientes';
import DetallePaciente from './pages/DetallePaciente';
import ListaEspera    from './pages/ListaEspera';
import CitasMedicas   from './pages/CitasMedicas';
import Reasignacion   from './pages/Reasignacion';
import Notificaciones from './pages/Notificaciones';

// Guard interno para rutas de doctor
function RutaDoctor({ children }) {
  const { usuario } = useAuth();
  if (!usuario) return <Navigate to="/login?rol=doctor" replace />;
  if (usuario.rol !== 'doctor') return <Navigate to="/portal-paciente" replace />;
  return children;
}

// Guard interno para rutas de paciente
function RutaPaciente({ children }) {
  const { usuario } = useAuth();
  if (!usuario) return <Navigate to="/login?rol=paciente" replace />;
  if (usuario.rol !== 'paciente') return <Navigate to="/dashboard" replace />;
  return children;
}

function AppRoutes() {
  return (
    <Routes>
      {/* Públicas */}
      <Route path="/"      element={<Welcome />} />
      <Route path="/login" element={<Login />} />

      {/* Portal paciente (sin Layout de médico) */}
      <Route path="/portal-paciente" element={
        <RutaPaciente><PortalPaciente /></RutaPaciente>
      } />

      {/* Portal médico (con Layout y Navbar) */}
      <Route path="/dashboard" element={
        <RutaDoctor>
          <Layout><Home /></Layout>
        </RutaDoctor>
      } />
      <Route path="/pacientes" element={
        <RutaDoctor>
          <Layout><Pacientes /></Layout>
        </RutaDoctor>
      } />
      <Route path="/pacientes/:id" element={
        <RutaDoctor>
          <Layout><DetallePaciente /></Layout>
        </RutaDoctor>
      } />
      <Route path="/lista-espera" element={
        <RutaDoctor>
          <Layout><ListaEspera /></Layout>
        </RutaDoctor>
      } />
      <Route path="/citas" element={
        <RutaDoctor>
          <Layout><CitasMedicas /></Layout>
        </RutaDoctor>
      } />
      <Route path="/reasignacion" element={
        <RutaDoctor>
          <Layout><Reasignacion /></Layout>
        </RutaDoctor>
      } />
      <Route path="/notificaciones" element={
        <RutaDoctor>
          <Layout><Notificaciones /></Layout>
        </RutaDoctor>
      } />

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}