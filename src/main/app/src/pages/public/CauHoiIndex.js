import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../components/notify/Notify';
import {Alphabetical} from '../../components/helpers/FieldValidate';
import PublicService from '../../services/PublicService';

export default class CauHoiIndex extends Component {
    constructor(props) {
        super(props);

        this.state = {
            cauHoiDTOs: []
        }
    }

    componentDidMount() {
        PublicService.listCauHoi()
        .then(response => {
            console.log(response);
            if (response?.data) {
                this.setState({
                    cauHoiDTOs: response.data
                })
            }
        })
        .catch((error) => {
            Notify.error(error.message)
        })
    }
    
    render() {
        return(
            <div class="container">
                {this.state.cauHoiDTOs.map((dto, index) => {
                        let cauHoi = dto.cauHoi;
                        let dapAn = dto.dapAns;
                        return(
                            <div key={index}>
                                <b>Câu {cauHoi.sttcauHoi}: </b>
                                {cauHoi.noiDungCauHoi}
                                {dapAn.sort((a,b) => 0.5 - Math.random())
                                    .map((dapAn, index) => {
                                        return(
                                            <div>
                                                <b>{Alphabetical(index + 1)}:</b>
                                                {dapAn.noiDungDapAn}
                                            </div>
                                        )
                                    })}
                            </div>
                    )
                    })}

            </div>
            // <div>
            //     <Form 
            //         onSubmit={() => {}}
            //         render={({ values, handleSubmit }) => (
            //         <form onSubmit={handleSubmit}>
                        
            //         </form>
            //     )}/>
            //</div>
        )
    }
}
