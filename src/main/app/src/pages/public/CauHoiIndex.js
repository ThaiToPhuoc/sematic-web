import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../components/notify/Notify';
import {Alphabetical} from '../../components/helpers/FieldValidate';
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';

export default class CauHoiIndex extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            cauHoiDTOs: [],
            KQ: '',
        }
    }

    componentDidMount() {
        PublicService.listCauHoi(this.state.id)
        .then(response => {
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

    getChuongId = (cauHoi) => {
        let id = cauHoi?.thuocTiet?.thuocChuong.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/chuong/${id}`}>{id}</Link>;
    }

    onPickDapAn = (dapAn, index, cauHoi) => {
        let dto = this.state.cauHoiDTOs.find(d => d.cauHoi.id === cauHoi.id);

        cauHoi['ispicked'] = 1;

        dto.dapAns.map((d) => {
            d['ispicked'] = 0
        })

        if(dapAn.ispicked === 1)
        {
            dapAn['ispicked'] = 0;
        }

        else{
            dapAn['ispicked'] = 1;
        }
        
        dto.dapAns.splice(index, 1, dapAn);
        dto.cauHoi = cauHoi;
        dto.dapAns.forEach((d) => {
            if(d['ispicked'] === 0)
            {
                d['style'] =
                {
                    color: 'black'
                }
            }

            if(d['ispicked'] === 1)
            {
                d['style'] =
                {
                    color: 'green'
                }
            }
        })
    
        this.setState({
            dto
        })
    }

    handleEntailmentRequest(e) {
         let dto = this.state.cauHoiDTOs;
        dto.forEach((ch) => {
            if(ch.ispicked === 1){
                ch.forEach((d) => {
                    if(d.ispicked == 1){
                        if(d.ketQua == 0)
                        {
                            d['style'] =
                            {
                                color: 'red'
                            }
                        }
                        else
                        {
                            d['style'] =
                            {
                                color: 'blue'
                            }
                        }
                    }
                    else
                    {
                        if(d.ketQua == 0)
                        {
                            d['style'] =
                            {
                                color: 'black'
                            }
                        }
                        else
                        {
                            d['style'] =
                            {
                                color: 'blue'
                            }
                        }
                    }
                })
            }
        })
        this.setState(
            {
                cauHoiDTOs: dto,
                KQ:'Đã nộp bài'
            }
        )
    }

    onClickNopBai = (e) => {
        let dto = this.state.cauHoiDTOs;
        let ketqua = '';
        let diem = 0;
        dto.forEach((ch) => {
            if(ch.cauHoi.ispicked === 1){
                ch.dapAns.forEach((d) => {
                    if(d.ispicked === 1){
                        if(d.ketQua === 0)
                        {
                            d['style'] =
                            {
                                color: 'red'
                            }
                            ch.cauHoi['kq'] = 'Bạn đã trả lời sai! Đáp án đúng: ' + d.noiDungDapAn;
                        }
                        else
                        {
                            d['style'] =
                            {
                                color: 'blue'
                            }
                            diem = diem + 1;
                            ch.cauHoi['kq'] = 'Chính xác!';
                        }
                    }
                    else
                    {
                        if(d.ketQua === 0)
                        {
                            d['style'] =
                            {
                                color: 'black'
                            }
                        }
                        else
                        {
                            d['style'] =
                            {
                                color: 'blue'
                            }
                        }
                    }
                })
            }

            else{
                ch.dapAns.forEach((d) => {
                        if(d.ketQua === 0)
                        {
                            d['style'] =
                            {
                                color: 'black'
                            }
                        }
                        else
                        {
                            d['style'] =
                            {
                                color: 'blue'
                            }
                            ch.cauHoi['kq'] = 'Bạn chưa làm câu này! Đáp án: ' + d.noiDungDapAn;
                        }
                })
                
            }
        })
        this.setState(
            {
                cauHoiDTOs: dto,
                KQ:'Đã nộp bài! Điểm số của bạn là: ' + diem + '/10'
            }
        )
        e.preventDefault();
    }

    render() {
        return(
            <div class="container">
                {this.state.cauHoiDTOs.map((dto, index) => {
                        let cauHoi = dto.cauHoi;
                        let dapAns = dto.dapAns;
                        return(
                            <div key={index} className ='my-5'>
                                <b>Câu {cauHoi.sttcauHoi}: </b>
                                {cauHoi.noiDungCauHoi}

                                {dapAns.map((dapAn, index) => {
                                        return(
                                            <div style={dapAn.style} onClick={() => this.onPickDapAn(dapAn, index, cauHoi)}>
                                                <b>{Alphabetical(index + 1)}:</b>
                                                {dapAn.noiDungDapAn}
                                            </div>
                                        )
                                })}
                                <div>
                                    <b>{cauHoi.kq}</b>
                                </div>
                            </div>
                        )
                    })}
                <button className='btn btn-warning' onClick= {(e) => {this.onClickNopBai(e)}}>
                    Nộp bài
                </button>
                <div>
                    {this.state.KQ}
                    
                </div>

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