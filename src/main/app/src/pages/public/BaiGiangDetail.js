import React, { Component } from 'react'
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';

export default class ChuongDetail extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            baiGiang: { }
        }
    }

    callAPI = () =>{
        PublicService.findBaiGiangById(this.state.id)
        .then(response => {
            if (response?.data) {
                console.log(response)
                this.setState({
                    baiGiang: response.data
                })
            }
        })
    }

    getChuongId = (chuong) => {
        let id = chuong?.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/chuong/${id}`}>Chương {chuong.sttchuong}: {chuong.noiDungChuong}</Link>;
    }

    componentDidMount() {
        this.callAPI();
    }

    render() {
        return (
            <div class = "container">
                <h2> Chương trình lớp: {this.state.baiGiang.chuongTrinh}</h2>
                <p>Học kỳ: {this.state.baiGiang.hocKy}</p>
                <p>Nội dung chương trình: </p>
                <div class = "container">
                    {this.state.baiGiang?.gomChuong?.sort((a, b) => a.sttchuong > b.sttchuong ? 1 : -1).map((chuong) => {
                        return(
                            <p>{this.getChuongId(chuong)}</p>
                        )
                    })}
                </div>
            </div>
        )
    }
}