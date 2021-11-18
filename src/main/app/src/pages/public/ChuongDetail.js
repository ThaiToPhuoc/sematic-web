import React, { Component } from 'react'
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';

export default class ChuongDetail extends Component {
    constructor(props) {
        super(props);

        this.state = {
            id: props.match?.params?.id ? props.match.params.id : '',
            chuongdto: { }
        }
    }

    getTietId = (tiet) => {
        let id = tiet?.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/tiet/${id}`}>Nội dung tiết học</Link>;
    }
    
    componentDidMount() {
        PublicService.findChuongById(this.state.id)
        .then(response => {
            if (response?.data) {
                console.log(response)
                this.setState({
                    chuongdto: response.data
                })
            }
        })
    }



    render() {
        return (
            <div class = "container">
                <h2>Chương {this.state.chuongdto?.sttchuong}: {this.state.chuongdto?.noiDungChuong}</h2>
                <div class = "container">
                    {this.state.chuongdto?.gomTiet?.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
                    .map((tiet) => {
                        return(
                            <div>
                                <p>Tiết {tiet.stttiet}: {tiet.noiDungTiet}  </p> 
                                <div class = "container">
                                    <p>{this.getTietId(tiet)}</p>
                                </div>
                            </div>
                        )
                    })}
                </div>
            </div>
        )
    }
}
