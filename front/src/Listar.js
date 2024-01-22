import React, { Component } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import './listar.css';

class ListUser extends Component {
  state = {
    usuarios: [],
    userPerfil: 'USER', // Adicione essa propriedade para armazenar o perfil do usuário logado
  };

  async remove(id, perfil) {
    const { userPerfil } = this.state;

    // Verificar se o perfil do usuário logado é ADMIN
    if (userPerfil !== 'ADMIN') {
      alert('Você não tem permissão para excluir usuários.');
      return;
    }

    // Confirmar exclusão
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

    // Simule o perfil do usuário logado (você pode obter essa informação durante o login)
    // Isso é apenas um exemplo e deve ser ajustado conforme a lógica real da sua aplicação
    this.setState({ userPerfil: 'ADMIN' });
  }

  render() {
    return (
      <tbody>
        <div className="tabela">
          <br></br>
          <div className="title">Usuários</div>
          <br></br>
          <hr></hr>
        </div>
        <div className="botoes"></div>
        <table>
          <tr>
            <th>Id</th>
            <th>Nome</th>
            <th>E-mail</th>
            <th>Cpf</th>
            <th>Perfil</th>
            <th>Ações</th>
          </tr>
          {this.state.usuarios.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.userName}</td>
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
        </table>
      </tbody>
    );
  }
}

export default ListUser;
