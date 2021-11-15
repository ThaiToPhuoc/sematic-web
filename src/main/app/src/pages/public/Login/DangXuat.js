import React, { Component } from 'react'
import UserService from '../../../services/UserService';

export default class DangXuat extends Component {

    logout = () => { 
        UserService.logout();

        window.location.reload();
    }

    render() {
        return (
            <div>
                <button 
                    className='btn btn-danger'
                    onClick={this.logout}
                >
                    LOGOUT
                </button>
            </div>
        )
    }
}
