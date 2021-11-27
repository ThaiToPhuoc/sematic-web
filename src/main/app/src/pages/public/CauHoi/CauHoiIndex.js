import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../../components/notify/Notify';
import {Alphabetical} from '../../../components/helpers/FieldValidate';
import PublicService from '../../../services/PublicService';
import { Link } from 'react-router-dom';
import './style.scss'

const color = {
    blue: '#396EB0',
    red: '#FF5403',
    green: '#28A745'
}

export default class CauHoiIndex extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            cauHoiDTOs: [],
            KQ: '',
            daNopBai: false
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
        if (this.state.daNopBai) {
            return;
        }

        let dto = this.state.cauHoiDTOs.find(d => d.cauHoi.id === cauHoi.id);

        cauHoi.ispicked = 1;

        dto.dapAns.forEach((d) => d.ispicked = 0 )

        dapAn.ispicked = dapAn.ispicked ? 0 : 1
        
        dto.dapAns.splice(index, 1, dapAn);

        dto.cauHoi = cauHoi;
        dto.dapAns.forEach((d) => d.style = d.ispicked ? { borderColor: color.blue } : { borderColor: 'transparent' } )
    
        this.setState({ dto })
    }

    onClickNopBai = (e) => {
        let dto = this.state.cauHoiDTOs;
        let diem = 0;
        const { blue, green, red } = color
        
        this.setState({
            daNopBai: true
        })
        dto.forEach((ch) => {

            let dapAnDung = ch.dapAns.find(d => d.ketQua)

            let kqMsgs = {
                sai: 'Bạn đã trả lời sai! Đáp án đúng: ' + dapAnDung.noiDungDapAn,
                dung: 'Chính xác!',
                chuaLam: 'Bạn chưa làm câu này! Đáp án: ' + dapAnDung.noiDungDapAn
            }
            ch.dapAns.forEach((d) => {

                d.style = { 
                    backgroundColor: 'none',
                    color: 'black'
                }

                if (ch.cauHoi.ispicked) {
                    if (d.ispicked) {
                        if (!d.ketQua) {
                            d.style = { 
                                backgroundColor: red,
                                color: 'white'
                            }
                            ch.cauHoi.kq = kqMsgs.sai

                        } else {
                            d.style = { 
                                backgroundColor: green,
                                color: 'white'
                            }
                            ch.cauHoi.kq = kqMsgs.dung
                            diem++
                        }
                    } else {
                        d.style = { 
                            backgroundColor: d.ketQua ? blue : 'none',
                            color:  d.ketQua ? 'white' : 'black'
                        }
                    }

                } else if (d.ketQua) {
                    ch.cauHoi.kq = kqMsgs.chuaLam
                    d.style = { 
                        backgroundColor: blue,
                        color: 'white'
                    }
                }
            })
        })

        this.setState({
            cauHoiDTOs: dto,
            KQ:'Đã nộp bài! Điểm số của bạn là: ' + diem + '/' + dto.length
        })

        console.log(dto.map(d => ({ cauHoi: d.cauHoi.id })))
    }

    render() {
        return(
            <div className="container py-4">
                <div className='row g-2'>
                    <div className='col-4'>
                        <div className='border rounded p-2'>
                            <div>
                                {this.state.KQ}
                            </div>
                            <button className='btn btn-warning' onClick= {this.onClickNopBai}>
                                Nộp bài
                            </button>
                        </div>
                    </div>
                    <div className='col-8'>
                        <div className='p-2 border rounded cau-hoi'>
                            {this.state.cauHoiDTOs.map((dto, index) => {
                                let cauHoi = dto.cauHoi;
                                let dapAns = dto.dapAns;
                                return(
                                    <div key={index} className ='my-3 p-1 rounded ch-container'>
                                        <h5 className='px-1'>
                                            <b>Câu {index + 1}: </b>
                                            {cauHoi.noiDungCauHoi}
                                        </h5>
                                        <hr />
                                        {dapAns.map((dapAn, index) => (
                                            <div
                                                style={dapAn.style} className='pointer dap-an px-1 rounded'
                                                onClick={() => this.onPickDapAn(dapAn, index, cauHoi)}
                                            >
                                                <b>{Alphabetical(index + 1)}:</b>
                                                &nbsp;
                                                {dapAn.noiDungDapAn}
                                            </div>
                                        ))}
                                        <div className='px-1'>
                                            <b>{cauHoi.kq}</b>
                                        </div>
                                    </div>
                                )
                            })}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}