import { faBars, faChalkboardTeacher, faHome, faSignOutAlt } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React, { Component } from 'react'
import "../style.scss"
import { withRouter } from "react-router-dom";
import UserService from '../../../../services/UserService';

const items = [
    {
        tag: 'home',
        icon: faHome,
        title: 'Trang chủ',
        path: '/'
    },
    {
        tag: 'bai-giang',
        icon: faChalkboardTeacher,
        title: 'Bài giảng',
        path: '/bai-giang'
    },
]

class AdminNavbar extends Component {
    constructor(props) {
        super(props)

        this.state = {
            isVisible: true
        }
    }

    toggle = () => {
        let { isVisible } = this.state;
        document.documentElement.style.setProperty('--nav-width', !isVisible ? '200px' : '3.1em')

        this.setState({
            isVisible: !isVisible
        })
    }

    select = (item) => {
        this.props.onSelect(item.tag);

        this.props.history.push(`/admin${item.path}`)
    }

    render() {
        return (
            <div className='nav--sidenav'>
                <div 
                    className='nav--toggle'
                    onClick={this.toggle}
                >
                    <span className='nav--icon' >
                        <FontAwesomeIcon className={`${this.state.isVisible ? 'rotate' : ''}`} icon={faBars} fixedWidth />
                    </span>
                </div>

                <div className='nav--items'>
                    {items.map(
                        (item, index) => (
                            <div key={index}
                                className={ `nav--item ${this.props.selected === item.tag ? 'active' : '' }`}
                                onClick={() => this.select(item)}
                            >
                                <span className='nav--icon'>
                                    <FontAwesomeIcon icon={item.icon} fixedWidth />
                                </span>
                                <span className='nav--text'>{item.title}</span>
                            </div>
                        )
                    )}
                    <hr />
                    <div 
                        className={ `nav--item`}
                        onClick={() => { UserService.logout(); window.location.reload(); }}
                    >
                                <span className='nav--icon'>
                                    <FontAwesomeIcon icon={faSignOutAlt} fixedWidth />
                                </span>
                                <span className='nav--text'>Đăng xuất</span>
                            </div>
                </div>
            </div>
        )
    }
}

export default withRouter(AdminNavbar)