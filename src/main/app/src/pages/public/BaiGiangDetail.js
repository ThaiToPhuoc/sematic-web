import React, { Component } from 'react'
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';

export default class ChuongDetail extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            baiGiangdto: { }
        }
    }

    callAPI = () =>{
        PublicService.findBaiGiangById(this.state.id)
        .then(response => {
            if (response?.data) {
                this.setState({
                    baiGiangdto: response.data
                })
            }
        })
    }

    getChuongId = (chuong) => {
        let id = chuong?.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/chuong/${id}`}>Chương {chuong.sttchuong}: {chuong.noiDungChuong}</Link>
    }

    getKiemTra = (kt) => {
        let id = kt?.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/cau-hoi/${id}`}>link</Link>
    }

    componentDidMount() {
        this.callAPI();
    }

    render() {
        return (
            <div class = "container">
               <h2> Chương trình lớp: {this.state.baiGiangdto?.baiGiang?.chuongTrinh}</h2>
                <p>Học kỳ: {this.state.baiGiangdto?.baiGiang?.hocKy}</p>
                <p>Nội dung chương trình: </p>
                <div class = "container">
                    {this.state.baiGiangdto?.baiGiang?.gomChuong?.sort((a, b) => a.sttchuong > b.sttchuong ? 1 : -1).map((chuong) => {
                        return(
                            <p>{this.getChuongId(chuong)}</p>
                        )
                    })}
                </div>
                <p>Kiểm tra: </p>
                <div class = "container">
                   {this.state?.baiGiangdto?.kiemTras?.map((kt, index)=>{
                       return(
                           <p>Đề {index + 1}: {this.getKiemTra(kt)}</p>
                       )
                   })}
                </div>

            </div>
        )
    }
}