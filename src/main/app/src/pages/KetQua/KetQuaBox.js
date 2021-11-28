import React, { Component } from 'react'
import { TruncateSharp } from '../../components/helpers/FieldValidate';
import './style.scss'

export default class KetQuaBox extends Component {

    reroute = (link) => {
        this.props.history.replace(link)
    }

    render() {
        const { 
            CHoi,
            DAnChon,
            DAnDung,
            GoiY 
        } = this.props;

        const classNames = `
            rounded border p-2 
            ${DAnChon?.id === DAnDung?.id ? 'correct' : 'wrong'}
        `

        return (
            <div className='my-1 col-2'>
                <div className={classNames}>
                    <b>Câu hỏi: {CHoi.noiDungCauHoi}</b>
                    <hr />
                    <p><b>Đáp án được chọn:</b> {DAnChon ? DAnChon.noiDungDapAn : 'Bỏ trống'}</p>
                    <p><b>Đáp án đúng:</b> {DAnDung?.noiDungDapAn}</p>
                    <hr />
                    <div className='px-2 goi-y rounded'>
                        {GoiY.chuongs.map(({ chuongId, noiDungChuong, tiets }) => {
                            let chuong = TruncateSharp(chuongId)
                            return (
                                <div key={chuong}>
                                    <h6>Chương:&nbsp;
                                        <br />
                                        <span 
                                            className='link' 
                                            onClick={() => this.reroute(`/bai-giang/${chuong}`)}
                                        >{chuong}</span>
                                    </h6>
                                    <h6>Tiết: </h6>
                                    <ul>
                                        {tiets.map(tiet => {
                                            let tietId = TruncateSharp(tiet.id)
                                            return (
                                                <li>
                                                    <span 
                                                        className='link'
                                                        onClick={() => this.reroute(`/tiet/${tietId}`)}
                                                    >{tietId}</span>
                                                </li>
                                            )
                                        })}
                                    </ul>
                                    <hr />
                                </div>
                            )
                        })}
                    </div>
                </div>
            </div>
        )
    }
}
