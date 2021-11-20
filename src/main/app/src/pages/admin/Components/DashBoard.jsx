import React, { Component } from 'react'
import { Route, Switch } from 'react-router'
import BaiGiangList from '../BaiGiang/BaiGiangList'
import Home from '../Home/Home'
import AdminNavbar from './Navbar/AdminNavbar'

export default class DashBoard extends Component {
    constructor(props) {
        super(props)

        let path = window.location.pathname
        path = path.substring(path.indexOf('/admin') + '/admin'.length + 1)
        path = path.split('/')[0]

        this.state = {
            selected: path
        }
    }

    onSelect = (tag) => {
        this.setState({
            selected: tag
        })
    }

    render() {
        return (
            <div style={{height: '100%'}}>
                <AdminNavbar onSelect={this.onSelect} selected={this.state.selected} />
                
                <div className='dashboard--main'>
                    <Switch>
                        <Route exact path='/admin/bai-giang' component={BaiGiangList} />
                        <Route path='/admin' component={Home} />
                    </Switch>
                </div>
            </div>
        )
    }
}
