import React from 'react';
import { Routes, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import ResetPassword from './ResetPassword';

function App() {
  return (
    <Routes>
      <Route path="/" exact element={<LoginPage />} />
      <Route path="/reset/password" exact element={<ResetPassword />} />

    </Routes>
  );
}

export default App;
