import React, { useState } from 'react';
import { connect } from 'react-redux';
import { authenticate, authFailure, authSuccess } from '../src/api/authActions';
import './loginpage.css';
import { userLogin } from '../src/authenticationService';
import { Alert, Spinner } from 'react-bootstrap';
import { Link } from "react-router-dom";
import NewUser from './NewUser';

const LoginPage = ({ loading, error, ...props }) => {
    const [values, setValues] = useState({
        userName: '',
        senha: ''
    });

    const handleSubmit = (evt) => {
        evt.preventDefault();
        props.authenticate();

        userLogin(values).then((response) => {
            console.log("response", response);
            if (response.status === 200) {
                props.setUser(response.data);
                props.history.push('/dasboard');
            } else {
                props.loginFailure('Something Wrong! Please Try Again');
            }
        }).catch((err) => {
            if (err && err.response) {
                switch (err.response.status) {
                    case 401:
                        console.log("401 status");
                        props.loginFailure("Authentication Failed. Bad Credentials");
                        break;
                    default:
                        props.loginFailure('Something Wrong! Please Try Again');
                }
            } else {
                props.loginFailure('Something Wrong! Please Try Again');
            }
        });
    };

    const handleChange = (e) => {
        e.persist();
        setValues(values => ({
            ...values,
            [e.target.name]: e.target.value
        }));
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <NewUser />
            </div>
            <section className="h-100">
                <div className="container h-100">
                    <div className="row justify-content-md-center h-100">
                        <div className="card-wrapper">
                            <div className="card fat">
                                <div className="card-body">
                                    <h4 className="card-title">Login</h4>
                                    <form className="my-login-validation" onSubmit={handleSubmit} noValidate={false}>
                                        <div className="form-group">
                                            <label htmlFor="email">Usuário</label>
                                            <input
                                                id="userName"
                                                type="text"
                                                className="form-control"
                                                minLength={5}
                                                value={values.userName}
                                                onChange={handleChange}
                                                name="userName"
                                                required
                                            />
                                            <div className="invalid-feedback">
                                                UserId is invalid
                                            </div>
                                        </div>
                                        <div className="form-group">
                                            <label>Senha
                                                {/* <Link to="**" className="animated-button9"></Link> */}
                                            
                                            </label>
                                            <input
                                                id="senha"
                                                type="password"
                                                className="form-control"
                                                minLength={6}
                                                value={values.senha}
                                                onChange={handleChange}
                                                name="senha"
                                                required
                                            />
                                            <div className="invalid-feedback">
                                                Password is required
                                            </div>
                                        </div>
                                        <div className="form-group">
                                            <div className="custom-control custom-checkbox">
                                                <br></br>
                                            </div>
                                        </div>
                                        <div className="form-group m-0">
                                            <button type="submit" className="btn btn-primary">
                                                Login
                                                {loading && (
                                                    <Spinner
                                                        as="span"
                                                        animation="border"
                                                        size="sm"
                                                        role="status"
                                                        aria-hidden="true"
                                                    />
                                                )}
                                            </button>
                                        </div>
                                        <div>  <Link to="**" className="animated-button9"></Link>
                                                Forgot Password?</div>
                                    </form>
                                    {error && (
                                        <Alert style={{ marginTop: '20px' }} variant="danger">
                                            {error}
                                        </Alert>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};

const mapStateToProps = ({ auth }) => {
    console.log("state ", auth);
    return {
        loading: auth.loading,
        error: auth.error
    };
};

const mapDispatchToProps = (dispatch) => {
    return {
        authenticate: () => dispatch(authenticate()),
        setUser: (data) => dispatch(authSuccess(data)),
        loginFailure: (message) => dispatch(authFailure(message))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginPage);
