import React, { Component } from 'react'
import Notify from '../../components/notify/Notify';
import PublicService from '../../services/PublicService';

export default class CauHoiIndex extends Component {
    constructor(props) {
        super(props);

        this.state = {
            cauHois: []
        }
    }

    componentDidMount() {
        PublicService.saveCauHoi({
            id: 'id',
            NoiDungCauHoi: 'abc',
            STTCauHoi: '1'
        })
    }
    
    render() {
        return (
            <div>
            </div>
        )
    }
}
