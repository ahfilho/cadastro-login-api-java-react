import React, { useState } from "react";
import axios from "axios";
import './NewUser.css';

const url = "http://localhost:8080/new/user";

const NewUser = () => {
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [cpf, setCpf] = useState("");
  const [perfil, setPerfil] = useState("USER"); // Valor padrão

  const formatCpf = (value) => {
    // Remove caracteres não numéricos
    const cleanedValue = value.replace(/[^\d]/g, "");

    // Aplica a máscara
    const formattedValue = cleanedValue.replace(
      /^(\d{3})(\d{3})(\d{3})(\d{2})$/,
      "$1.$2.$3-$4"
    );

    return formattedValue;
  };

  const handleCpfChange = (e) => {
    const formattedCpf = formatCpf(e.target.value);
    setCpf(formattedCpf);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const user = {
      userName,
      email,
      senha,
      cpf,
      perfil,
    };

    try {
      const response = await axios.post(url, user);

      console.log(response.data);

      alert("SALVO COM SUCESSO -- apenas para testes!");
      setUserName("");
      setEmail("");
      setSenha("");
      setCpf("");
      setPerfil("USER"); // Resetar para o valor padrão
    } catch (error) {
      console.error("Erro ao salvar usuário:", error);
    }
  };

  return (
    <div className="login-page ">
      <div className="new-user-form .brand">
        <section className="h-100">
          <div className="container h-100">
            <div className="row justify-content-md-center h-100">
              <div className="card-wrapper">
                <div className="card fat">
                  <div className="card-body">
                    <h4 className="card-title">Cadastrar</h4>
                    <form id="formulario" onSubmit={handleSubmit} className="login-form">
                      <div className="form-group">
                        <input
                          type="text"
                          name="userName"
                          id="userName"
                          value={userName}
                          className="form-control"
                          placeholder="userName"
                          onChange={(e) => setUserName(e.target.value)}
                        />
                      </div>
                      <div className="inputs">
                        <input
                          type="text"
                          name="email"
                          id="email"
                          className="form-control"
                          value={email}
                          placeholder="E-mail"
                          onChange={(e) => setEmail(e.target.value)}
                        />
                      </div>
                      <div className="inputs">
                        <input
                          type="text"
                          name="senha"
                          id="senha"
                          className="form-control"
                          value={senha}
                          placeholder="Senha"
                          onChange={(e) => setSenha(e.target.value)}
                        />
                      </div>
                      <div className="inputs">
                        <input
                          type="text"
                          name="cpf"
                          id="cpf"
                          className="form-control"
                          value={cpf}
                          placeholder="Cpf"
                          onChange={handleCpfChange}
                        />
                      </div>
                      <div className="inputs">
                        <label>Perfil</label>
                        <div>
                          <label>
                            <input
                              type="radio"
                              name="perfil"
                              value="admin"
                              checked={perfil === "admin"}
                              onChange={() => setPerfil("admin")}
                            />
                            Administrador
                          </label>
                          <label>
                            <input
                              type="radio"
                              name="perfil"
                              value="usuario"
                              checked={perfil === "usuario"}
                              onChange={() => setPerfil("usuario")}
                            />
                            Usuário
                          </label>
                        </div>
                      </div>
                      <br></br>
                      <div className="inputs">
                        <input
                          className="btn btn-primary"
                          type="submit"
                          value="Submit"
                        ></input>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div >
    </div >
  );
};

export default NewUser;
