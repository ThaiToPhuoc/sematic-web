import React, { Component } from 'react'
import { Form } from 'react-final-form';
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
                <Form 
                    onSubmit={() => {}}
                    render={({ values, handleSubmit }) => (
                    <form onSubmit={handleSubmit}>
                        
                    </form>
                )}/>
            </div>
        )
    }
}
