import React, { Component } from 'react'
import { Link } from 'react-router-dom';
import PublicService from '../../services/PublicService';

export default class TietDetail extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            tiet: { }
        }
    }

    componentDidMount() {
        PublicService.findTietById(this.state.id)
        .then(response => {
            if (response?.data) {
                console.log(response)
                this.setState({
                    tiet: response.data
                })
            }
        })
    }

    render() {
        return (
            <div class = "container">
                <h2>Tiết {this.state.tiet?.stttiet}: {this.state.tiet?.noiDungTiet}</h2>
                <p>{this.state.tiet?.link}</p>
            </div>
        )
    }
}
