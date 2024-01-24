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
      <Route path="/" exact element={<LoginPage />} />
      <Route path="/reset/password" exact element={<ResetPassword />} />
      <Route path="/new/user" exact element={<NewUser />} />
      <Route path="/hola" exact element={<Hola />} />
      <Route path="/list" exact element={<Listar />} />
      <Route path="/forgot/password" exact element={<ForgotPass/>}/> 

    </Routes>
  );
}

export default App;
