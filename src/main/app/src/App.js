import logo from './logo.svg';
import './App.css';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import React, { Component } from 'react';
import Index from './pages/public/Index';
import Notify from './components/notify/Notify';
import { BrowserRouter } from 'react-router-dom';

export default class App extends Component {
  componentDidMount() {
    Notify.notifications.subscribe((alert) => alert instanceof Function && alert());
  }

  render() {
    return (
      <div className="position-relative app-body">
        <BrowserRouter>
          <Index />
        </BrowserRouter>

        <ToastContainer autoClose={3500} theme='colored' />
      </div>
    );
  }
}
