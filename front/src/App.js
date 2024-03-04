import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import ResetPassword from './pages/ResetPassword';
import NewUser from './pages/NewUser';
import Hola from './pages/Hola';
import Listar from './pages/Listar';
import ForgotPass from './pages/ForgotPass';
function App() {
  return (
    <Routes>
      <Route path="/"  element={<LoginPage />} />
      <Route path="/reset/password"  element={<ResetPassword />} />
      <Route path="/new/user"  element={<NewUser />} />
      <Route path="/hola"  element={<Hola />} />
      <Route path="/list"  element={<Listar />} />
      <Route path="/forgot/password"  element={<ForgotPass/>}/> 

    </Routes>
  );
}

export default App;
