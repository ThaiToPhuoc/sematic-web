/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { Component } from 'react'
import Notify from '../../components/notify/Notify';
import PublicService from '../../services/PublicService';
import { Link, withRouter } from 'react-router-dom';
import DangXuat from './Login/DangXuat';
import { TruncateSharp } from '../../components/helpers/FieldValidate';
import BasicSearch from '../../components/Search/BasicSearch';

const linkStyle = {
    textDecoration: 'none', 
    color: 'white', 
    margin: "0 .5rem"
}

class Navbar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            BaiGiang: []
        }
    }

    reroute = (id) => {
        this.props.history.push(`/bai-giang/${id}`)
        window.location.reload()
    }
    
    componentDidMount() {
        PublicService.listBaiGiang()
        .then(response => {
            if (response?.data) {
                this.setState({
                    BaiGiang: response.data
                })
            }
        })
        .catch((error) => {
            Notify.error(error.message)
        })
    }

    render() {
        return (
            <div>
                 <nav className="navbar navbar-expand-lg navbar-light sticky-top mx-auto"
                    style={{backgroundColor: '#e3f2fd'}}
                 >
                    <div class="container-fluid">
                        <Link className='navbar-brand' to='/'>Trang chủ</Link>
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul className="navbar-nav me-auto">
                                <li class="nav-item dropdown">
                                    <a 
                                        class="nav-link dropdown-toggle" 
                                        href="#" id="navbarDropdown" 
                                        role="button" 
                                        data-bs-toggle="dropdown" 
                                        aria-expanded="false"
                                    >
                                        Chương trình
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    {this.state.BaiGiang.map((bg) => {
                                        let id = TruncateSharp(bg.id)
                                        return (
                                            <li onClick={() => this.reroute(id)} >
                                                <span className='dropdown-item pointer'>
                                                    Lớp {bg.chuongTrinh} - HK {bg.hocKy}
                                                </span>
                                            </li>
                                        )
                                    })}
                                    </ul>
                                </li>

                                <li className="nav-item ms-1">
                                    <Link to='/kiem-tra' className='nav-link'>Kết quả kiểm tra</Link>
                                </li>
                            </ul>
                        
                            <span className='me-3 w-50'>
                                <BasicSearch />
                            </span>
                            <span className='navbar-text'>
                                <DangXuat />
                            </span>
                        </div>
                    </div>
                </nav>
            </div>
        )
    }
}

export default withRouter(Navbar)