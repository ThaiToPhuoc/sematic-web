import React, { Component } from 'react'
import logo from '../Login/login.png';
import UserService from '../../../services/UserService';

export default class DangXuat extends Component {

    logout = () => { 
        UserService.logout();

        window.location.reload();
    }

    render() {
        return (
            <div>
                <nav class="navbar navbar-expand-md navbar-light bg-light sticky-top">
				<div class="container-fluid">
                <button 
                    className='btn btn-danger'
                    onClick={this.logout}
                >
                    LOGOUT
                    <img alt='logout' src={logo} width="40" height="40" />
                </button>
                </div>
			    </nav>
            </div>
        )
    }
}
