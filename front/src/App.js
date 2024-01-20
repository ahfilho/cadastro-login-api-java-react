import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import ResetPassword from './ResetPassword';
import NewUser from './NewUser';
import Hola from './Hola';
function App() {
  return (
    <Routes>
      <Route path="/" exact element={<LoginPage />} />
      <Route path="/reset/password" exact element={<ResetPassword />} />
      <Route path="/new/user" exact element={<NewUser />} />
      <Route path="/hola" exact element={<Hola />} />

    </Routes>
  );
}

export default App;
