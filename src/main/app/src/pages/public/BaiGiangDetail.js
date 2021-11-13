import React, { Component } from 'react'
import PublicService from '../../services/PublicService';

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

    componentDidMount() {
        this.callAPI();
    }

    render() {
        return (
            <div class = "container">
                <h2> Chương trình lớp: {this.state.baiGiang.chuongTrinh}</h2>
                <p>Học kỳ: {this.state.baiGiang.hocKy}</p>
                <p>Nội dung chương trình: </p>
            </div>
        )
    }
}