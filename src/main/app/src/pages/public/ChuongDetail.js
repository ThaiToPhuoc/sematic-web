import React, { Component } from 'react'
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';
import VideoStream from '../public/Stream/VideoStream';
import ReactQuill from 'react-quill';

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
        return <Link to={`/tiet/${id}`}>{tiet.link}</Link>
    }
    
    componentDidMount() {
        PublicService.findChuongById(this.state.id)
        .then(response => {
            if (response?.data) {
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
                <br />
                <div class = "container">
                    {this.state.chuongdto?.gomTiet?.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
                    .map((tiet) => {
                        return(
                            <div>
                                <h5>Tiết {tiet.stttiet}: {tiet.link}  </h5> 
                                <ReactQuill 
                                    value={tiet?.noiDungTiet} 
                                    readOnly={true}
                                    theme={"bubble"}
                                />
                            </div>
                        )
                    })}
                    {this.state.chuongdto?.video && <VideoStream id='video' url={this.state.chuongdto.video} controls loop className='w-100 border shadow' />}
                </div>
            </div>
        )
    }
}
