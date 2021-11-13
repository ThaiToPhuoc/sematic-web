import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../components/notify/Notify';
import {Alphabetical} from '../../components/helpers/FieldValidate';
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';

export default class BaiGiangIndex extends Component {
    constructor(props) {
        super(props);

        this.state = {
            BaiGiang: []
        }
    }

    getBaiGiangId = (bg) => {
        let id = bg.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/bai-giang/${id}`}><h2>Chương trình lớp: {bg.chuongTrinh}</h2></Link>;
    }
    
    componentDidMount() {
        PublicService.listBaiGiang()
        .then(response => {
            if (response?.data) {
                this.setState({
                    BaiGiang: response.data
                })
            }
        })
        .catch((error) => {
            Notify.error(error.message)
        })
    }

    render() {
        return (
            <div class='container'>
                {this.state.BaiGiang.map((bg) => {
                    return(
                        <div>
                            <h2>{this.getBaiGiangId(bg)}</h2>
                            <p>Học kỳ: {bg.hocKy}</p>
                            <p>Được soạn bởi: {bg.duocSoanBoi.ho} {bg.duocSoanBoi.ten}</p>
                        </div>
                    )
                })}
            </div>
        )
    }
}
