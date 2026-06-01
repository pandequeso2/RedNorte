import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

// Usuarios simulados (sin backend de auth real)
const USUARIOS = [
  { id: 1, rut: '12345678-9', nombre: 'Carlos Mendoza',  rol: 'paciente',  pacienteId: 1 },
  { id: 2, rut: '98765432-1', nombre: 'María González',  rol: 'paciente',  pacienteId: 2 },
  { id: 3, rut: 'DOCTOR-001', nombre: 'Dr. Héctor Vidal', rol: 'doctor',   pacienteId: null },
  { id: 4, rut: 'DOCTOR-002', nombre: 'Dra. Laura Espinoza', rol: 'doctor', pacienteId: null },
];

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);

  const login = (rut, password) => {
    // Password simulada: "1234" para todos
    if (password !== '1234') return false;
    const encontrado = USUARIOS.find(u => u.rut === rut);
    if (!encontrado) return false;
    setUsuario(encontrado);
    return true;
  };

  const logout = () => setUsuario(null);

  return (
    <AuthContext.Provider value={{ usuario, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);