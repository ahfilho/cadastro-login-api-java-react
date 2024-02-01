import React, { Component } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import './listar.css';

class ListUser extends Component {
  state = {
    usuarios: [],
    userPerfil: 'USER',
  };
  async remove(id, perfil) {
    const { userPerfil } = this.state;
  
    // Verificar se o usuário está autenticado e tem perfil de administrador
    if (!userPerfil || userPerfil !== 'ADMIN') {
      alert('Você não tem permissão para excluir usuários.');
      return;
    }
  
    const confirmDelete = window.confirm("Deseja mesmo excluir?");
    if (confirmDelete) {
      axios
        .delete(`http://localhost:8080/new/user/${id}`)
        .then(() => {
          let updatedUsers = [...this.state.usuarios].filter((i) => i.id !== id);
          this.setState({ usuarios: updatedUsers });
        })
        .catch((error) => {
          console.error("Erro ao excluir usuário:", error);
        });
    }
  }
  
  
  

  componentDidMount() {
    axios.get("http://localhost:8080/new/user/todos").then((res) => {
      const usuarios = res.data;
      this.setState({ usuarios });
    });

    this.setState({ userPerfil: 'ADMIN' });
  }

  render() {
    return (
      <div className="tabela-container">
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
            {this.state.usuarios.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.firstName}</td>
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
                  <button onClick={() => this.remove(user.id, user.perfil)} className="btn btn-danger">
                    <i className="fas fa-eraser"></i>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

export default ListUser;
