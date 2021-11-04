import React, { Component } from 'react'
import Notify from '../../components/notify/Notify';
import CauHoiIndex from './CauHoiIndex';

export default class Index extends Component {
    componentDidMount() {
        Notify.info('Xin chào!');
    }

    render() {
        return (
            <div>
                <CauHoiIndex />
            </div>
        )
    }
}
