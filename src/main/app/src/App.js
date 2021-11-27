import logo from './logo.svg';
import './App.css';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import React, { Component } from 'react';
import Index from './pages/public/Index';
import Notify from './components/notify/Notify';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import PrivateRoute from './components/axios/PrivateRoute';
import DangNhap from './pages/public/Login/DangNhap';
import DangKy from './pages/public/Login/DangKy';
import DashBoard from './pages/admin/Components/DashBoard';

export default class App extends Component {
  componentDidMount() {
    Notify.notifications.subscribe((alert) => alert instanceof Function && alert());
  }

  render() {
    return (
      <div className="position-relative app-body">
        <BrowserRouter>
          <Switch>
            <Route exact path='/dang-nhap' component={DangNhap} />
            <Route exact path='/dang-ky' component={DangKy} />
            
            <PrivateRoute path='/admin' component={DashBoard} />
            
            <PrivateRoute path='/' component={Index} />
          </Switch>
        </BrowserRouter>

        <ToastContainer autoClose={3500} theme='colored' />
      </div>
    );
  }
}
