import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../components/notify/Notify';
import {Alphabetical} from '../../components/helpers/FieldValidate';
import PublicService from '../../services/PublicService';
import { thisExpression } from '@babel/types';

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
    
    getRandomDapAn = (dapAns)=> {
        return dapAns.sort((a,b) => 0.5 - Math.random());
    }

    onPickDapAn = (dapAn, index, cauHoi) => {

        cauHoi['ispicked'] = true

        if(dapAn.ketQua === 1){
            dapAn['style'] = {
                color: 'green'
            }
        } else {
            dapAn['style'] = {
                color: 'red'
            }
        }

        let dto = this.state.cauHoiDTOs.find(d => d.cauHoi.id === cauHoi.id);

        dto.dapAns.splice(index, 1, dapAn);
        this.setState({
            dto
        })
    }

    render() {
        return(
            <div class="container">
                {this.state.cauHoiDTOs.map((dto, index) => {
                        let cauHoi = dto.cauHoi;
                        let dapAns = dto.dapAns;
                        return(
                            <div key={index}>
                                <b>Câu {cauHoi.sttcauHoi}: </b>
                                {cauHoi.noiDungCauHoi}

                                {dapAns.map((dapAn, index) => {
                                        return(
                                            <div style={dapAn.style} onClick={() => this.onPickDapAn(dapAn, index,cauHoi)}>
                                                <b>{Alphabetical(index + 1)}:</b>
                                                {dapAn.noiDungDapAn}
                                            </div>
                                        )
                                })}

                                {dapAns.map((dapAn, index) =>{
                                    return(
                                        dapAn.ketQua === 1
                                        ?<div>
                                            <b>Đáp án đúng: {Alphabetical(index + 1)}</b>
                                        </div>
                                        : <></>
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
