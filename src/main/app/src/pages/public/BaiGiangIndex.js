import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../components/notify/Notify';
import {Alphabetical} from '../../components/helpers/FieldValidate';
import PublicService from '../../services/PublicService';
import { Link } from 'react-router-dom';
import Accordion from '../../components/Accordion';

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
        return <Link to={`/bai-giang/${id}`} style={{ textDecoration: 'none', color: 'white'}}><li class="nav-item">lớp {bg.chuongTrinh}</li></Link>;
    }
    
    componentDidMount() {
        PublicService.listBaiGiang()
        .then(response => {
            if (response?.data) {
                console.log(response)
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
            <div>
                <div class = 'container'>
                    <h2 class="text-center">Thông tin và tin học</h2>
                    <p>1. Thông tin là gì?</p>
                    <ul>
                        <li> • Thông tin là tất cả những gì đem lại sự hiểu biết về thế giới xung quanh (sự vật, sự kiện, …) và về thế giới con người.</li>

                        <li> • Thông tin có mặt ở khắp xung quanh chúng ta: sách báo, tạp chí, internet, …</li>
                    </ul>
                    <p>2. Hoạt động thông tin của con người</p>
                    <ul>
                        <li> • Việc tiếp nhận, xử lý, lưu trữ và truyền (trao đổi) thông tin được gọi chung là hoạt động thông tin.</li>

                        <li> • Hoạt động thông tin diễn ra đối với mỗi người và là nhu cầu thiết yếu.</li>
                        <li> Xử lý thông tin có vai trò quan trọng nhất, mục đích là đem lại sự hiểu biết cho con người để có những kết luận, quyết định cần thiết.</li>
                        <li>• Thông tin trước khi xử lý được gọi là thông tin vào, sau khi thông tin được xử lý được gọi là thông tin ra.</li>
                    </ul>
                </div>
                <div className='container'>
                    <h2 class="text-center">Danh sách các bài giảng</h2>
                    {
                        this.state.BaiGiang.map((bg) =>{
                            return(
                                <Accordion title={`Lớp ${bg.chuongTrinh}`}>
                                    {
                                        bg.gomChuong.map((chuong) =>{
                                            return(
                                                <div className='container'>
                                                    <h3>Chương {chuong.sttchuong}:</h3>
                                                    {chuong.gomTiet?.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
                                                    .map((tiet) => {
                                                        return(
                                                            <div>
                                                                <h4>Tiết {tiet.stttiet}: {tiet.noiDungTiet}  </h4>
                                                                <p>{tiet.link}</p>
                                                            </div>
                                                        )
                                                    })}
                                                </div>
                                            )
                                        })
                                    }
                                </Accordion>
                            )
                        })
                    }   
                    
                </div>
            </div>
        )
    }
}
