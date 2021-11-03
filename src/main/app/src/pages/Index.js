import React, { Component } from 'react'
import Notify from '../components/notify/Notify';

export default class Index extends Component {
    componentDidMount() {
        Notify.info('Xin chào!');
    }

    render() {
        return (
            <div>
                
            </div>
        )
    }
}
