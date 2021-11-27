import React, { Component } from 'react'
import Notify from '../../components/notify/Notify';
import PublicService from '../../services/PublicService';
import { Link, withRouter } from 'react-router-dom';
import DangXuat from './Login/DangXuat';
import { TruncateSharp } from '../../components/helpers/FieldValidate';

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
                 <nav className="navbar navbar-expand-md navbar-light bg-warning sticky-top mx-auto">
                    <div className="container-fluid">
                    <div className="collapse navbar-collapse" id="navbarResponsive">
						<ul className="navbar-nav">
							<li className="nav-item">
								<Link to={`/`} style={{ textDecoration: 'none' , color: 'white', margin: "2rem", fontSize: '30px'}}>Trang chủ</Link>
							</li>
                            {this.state.BaiGiang.map((bg) => {
                                let id = TruncateSharp(bg.id)
                                return (
                                    <li 
                                        className="nav-item pointer" 
                                        onClick={() => this.reroute(id)}
                                        style={{ 
                                            textDecoration: 'none', 
                                            color: 'white', 
                                            fontSize: '30px'}}
                                        >lớp {bg.chuongTrinh}</li>
                                )
                            })}
							<li className="nav-item ms-1">
								<Link style={{ textDecoration: 'none' ,color: 'white', margin: "2rem", fontSize: '30px'}}>Kiểm tra</Link>
							</li>
                            
                            <div className='ms-auto'>
                                <li className="nav-item ms-auto">
                                    <DangXuat />
                                </li>
                            </div>
						</ul>
					</div>
                    </div>
                </nav>
            </div>
        )
    }
}

export default withRouter(Navbar)