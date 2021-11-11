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
                this.setState({
                    chuong: response.data
                })
            }
        })
    }

    render() {
        return (
            <div>
                URL: {this.state.id}
                <br />
                Object: {this.state.chuong.id}
            </div>
        )
    }
}