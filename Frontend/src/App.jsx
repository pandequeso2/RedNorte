import React from 'react';

function App() {
  return (
    <div className="min-h-screen">
      {/* Barra de Navegación Principal */}
      <nav className="bg-medical-main text-white shadow-md">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center gap-2">
              {/* Ícono simple (puedes reemplazarlo por un logo después) */}
              <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path>
              </svg>
              <span className="font-bold text-xl tracking-wider">RedNorte</span>
            </div>
            
            <div className="hidden md:flex space-x-8">
              <a href="#" className="hover:text-medical-light transition-colors px-3 py-2 rounded-md font-medium">Dashboard</a>
              <a href="#" className="hover:text-medical-light transition-colors px-3 py-2 rounded-md font-medium">Pacientes</a>
              <a href="#" className="hover:text-medical-light transition-colors px-3 py-2 rounded-md font-medium">Lista de Espera</a>
            </div>

            <div>
              <button className="bg-white text-medical-main px-4 py-2 rounded-lg font-semibold hover:bg-medical-light transition-colors shadow-sm">
                Cerrar Sesión
              </button>
            </div>
          </div>
        </div>
      </nav>

      {/* Contenido principal de prueba */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-8 text-center">
          <h1 className="text-3xl font-bold text-gray-800 mb-4">Bienvenido al Sistema Médico</h1>
          <p className="text-gray-600">Tailwind CSS y nuestra paleta de colores están configurados correctamente.</p>
        </div>
      </main>
    </div>
  );
}

export default App;