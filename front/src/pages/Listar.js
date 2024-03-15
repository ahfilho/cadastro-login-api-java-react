import React, { Component } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import './listar.css';

class Listar extends Component {
  state = {
    usuarios: [],
    userType: 'USER',
    showMessage: false,
    message: '',
    listaVazia: false,
    error: null
  };

  async remove(id, perfil) {
    const { userType } = this.state;

    if (!userType) {
      this.showMessage('Você não está logado.');
      return;
    }

    if (userType !== 'USER') {
      this.showMessage('Você precisa ser um Administrador para poder exluir ou alterar.');
      return;
    }

    const confirmDelete = window.confirm("Deseja mesmo excluir?");
    if (confirmDelete) {
      try {
        await axios.delete(`http://localhost:8080/new/user/${id}`);
        let updatedUsers = [...this.state.usuarios].filter((i) => i.id !== id);
        this.setState({ usuarios: updatedUsers });
      } catch (error) {
        console.error("Erro ao excluir usuário:", error);
        this.showMessage('Erro ao excluir usuário. Por favor, tente novamente mais tarde.');
        return;
      }
    }
  }

  showMessage(message) {
    this.setState({
      showMessage: true,
      message
    });

    setTimeout(() => {
      this.setState({
        showMessage: false,
        message: ''
      });
    }, 3000); // 3 segundos
  }

  async componentDidMount() {
    try {
      const res = await axios.get("http://localhost:8080/new/user/todos");
      const usuarios = res.data;
      if (usuarios.length === 0) {
        this.setState({ usuarios: [], listaVazia: true });
      } else {
        this.setState({ usuarios: usuarios, listaVazia: false });
      }
    } catch (error) {
      console.error("Erro ao buscar usuários:", error);
      this.setState({ error: "Erro ao buscar usuários. Por favor, tente novamente mais tarde." });
    }

    this.setState({ userPerfil: 'ADMIN' }); // Aqui você deve definir o perfil do usuário logado vindo do backend
  }

  render() {
    const { showMessage, listaVazia, message, error } = this.state;

    return (
    
        <div className="tabela-container">
          {showMessage && <div className="message">{message}</div>}
          <div className="tabela">
            <br></br>
            <div className="title">Usuários</div>
            <br></br>
            <hr></hr>
          </div>
          <div className="botoes"></div>
          <table>
            <thead>
              <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Usuário</th>
                <th>E-mail</th>
                <th>Cpf</th>
                <th>Perfil</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
            <div>
        {error && (
          <div className="error-message">
            <p>{error}</p>
          </div>
        )}</div>
              {this.state.usuarios.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.firstName + " " + user.lastName}</td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.cpf}</td>
                  <td>{user.profile}</td>
                  <td>
                    <Link to={`/userEdit/${user.id}`} className="btn btn-sucess">
                      <i className="far fa-edit"></i>
                    </Link>
                  </td>
                  
                  <td>
                    {this.state.userPerfil === 'ADMIN' && ( // Apenas o administrador pode ver o botão de exclusão
                      <button onClick={() => this.remove(user.id, user.profile)} className="btn btn-danger">
                        <i className="fas fa-eraser"></i>
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
    );
  }
}

export default Listar;
