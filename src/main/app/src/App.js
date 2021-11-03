import logo from './logo.svg';
import './App.css';
import { ToastContainer } from 'react-toastify';

export default class App {
  render() {
    return (
      <div className="App">
        <ToastContainer autoClose={3500} />
      </div>
    );
  }
}
