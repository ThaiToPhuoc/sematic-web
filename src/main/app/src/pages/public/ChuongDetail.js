import React, { Component } from 'react'
import PublicService from '../../services/PublicService';

export default class ChuongDetail extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            chuong: { }
        }
    }

    componentDidMount() {
        PublicService.findChuongById(this.state.id)
        .then(response => {
            if (response?.data) {
                console.log(response)
                this.setState({
                    chuong: response.data
                })
            }
        })
    }

    render() {
        return (
            <div class = "container">
                <h2>Chương {this.state.chuong?.sttchuong}: {this.state.chuong?.noiDungChuong}</h2>
                <div class = "container">
                    {this.state.chuong?.gomTiet?.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
                    .map((tiet) => {
                        return(
                            <p>Tiết {tiet.stttiet}: {tiet.noiDungTiet}</p>
                        )
                    })}
                </div>
            </div>
        )
    }
}
