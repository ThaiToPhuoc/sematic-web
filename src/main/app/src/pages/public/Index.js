import React, { Component } from 'react'
import { Route, Switch } from 'react-router';
import { Link } from 'react-router-dom';
import Notify from '../../components/notify/Notify';
import CauHoiUpdate from '../cauhoi/CauHoiUpdate';
import CauHoiIndex from './CauHoiIndex';

export default class Index extends Component {
    componentDidMount() {
        Notify.info('Xin chào!');
    }

    render() {
        return (
            <div>
                <Link to='/admin/cau-hoi/cap-nhat' >Cập nhật câu hỏi</Link>
                <Switch>
                    <Route exact path='/admin/cau-hoi/cap-nhat' component={CauHoiUpdate} />
                </Switch>
            </div>
        )
    }
}
