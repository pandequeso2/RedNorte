import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './component/Layout';
import Home            from './pages/Home';
import Pacientes       from './pages/Pacientes';
import DetallePaciente from './pages/DetallePaciente';
import ListaEspera     from './pages/ListaEspera';
import CitasMedicas    from './pages/CitasMedicas';
import Reasignacion    from './pages/Reasignacion';
import Notificaciones  from './pages/Notificaciones';

export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/"                    element={<Home />} />
          <Route path="/pacientes"           element={<Pacientes />} />
          <Route path="/pacientes/:id"       element={<DetallePaciente />} />
          <Route path="/lista-espera"        element={<ListaEspera />} />
          <Route path="/citas"               element={<CitasMedicas />} />
          <Route path="/reasignacion"        element={<Reasignacion />} />
          <Route path="/notificaciones"      element={<Notificaciones />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}