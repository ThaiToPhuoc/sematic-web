import React, { Component } from 'react'
import logo from '../Login/login.png';
import UserService from '../../../services/UserService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSignOutAlt } from '@fortawesome/free-solid-svg-icons';

export default class DangXuat extends Component {

    logout = () => { 
        UserService.logout();

        window.location.reload();
    }

    render() {
        return (
            <span 
                className='text-danger pointer me-5'
                onClick={this.logout}
            >
                <b><FontAwesomeIcon icon={faSignOutAlt} /> Đăng xuất</b>
            </span>
        )
    }
}
