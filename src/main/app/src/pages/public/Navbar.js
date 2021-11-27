import React, { Component } from 'react'
import Notify from '../../components/notify/Notify';
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';
import DangXuat from './Login/DangXuat';

export default class Navbar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            BaiGiang: []
        }
    }

    getBaiGiangId = (bg) => {
        let id = bg.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/bai-giang/${id}`} style={{ textDecoration: 'none', color: 'white', fontSize: '30px'}}><li class="nav-item">lớp {bg.chuongTrinh}</li></Link>;
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
                 <nav class="navbar navbar-expand-md navbar-light bg-warning sticky-top mx-auto">
                    <div class="container-fluid">
                    <div class="collapse navbar-collapse" id="navbarResponsive">
						<ul class="navbar-nav">
							<li class="nav-item">
								<Link to={`/`} style={{ textDecoration: 'none' , color: 'white', margin: "2rem", fontSize: '30px'}}>Trang chủ</Link>
							</li>
                            {this.state.BaiGiang.map((bg) => {
                                return(
                                    <div>
                                        {this.getBaiGiangId(bg)}
                                    </div>
                                )
                            })}
							<li class="nav-item ms-1">
								<Link style={{ textDecoration: 'none' ,color: 'white', margin: "2rem", fontSize: '30px'}}>Kiểm tra</Link>
							</li>
                            
                            <div className='ms-auto'>
                                <li class="nav-item ms-auto">
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

