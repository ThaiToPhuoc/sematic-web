import React, { Component } from 'react'
import { Route, Switch } from 'react-router';
import { Link } from 'react-router-dom';
import Notify from '../../components/notify/Notify';
import CauHoiUpdate from '../cauhoi/CauHoiUpdate';
import BaiGiangIndex from './BaiGiang/BaiGiangIndex';
import CauHoiIndex from './CauHoi/CauHoiIndex';
import ChuongDetail from './ChuongDetail';
import BaiGiangDetail from './BaiGiangDetail';
import TietDetail from './TietDetail';
import PrivateRoute from '../../components/axios/PrivateRoute';
import DangNhap from './Login/DangNhap';
import UserService from '../../services/UserService';
import DangXuat from './Login/DangXuat';
import Navbar from './Navbar';
import KetQuaIndex from '../KetQua/KetQuaIndex';
import TimKiem from './TimKiem/TimKiem';

export default class Index extends Component {

    render() {
        return (
            <div>
                {/* <h2 class="sub-title">Trắc nghiệm Tin học lớp 6:</h2>
                <p><b class="color-green">Bài 1:</b> Thông tin có thể giúp cho con người:</p>
                <p>A.	Nắm được quy luật của tự nhiên và do đó trở nên mạnh mẽ hơn.</p>
                <p>B.	Hiểu biết về cuộc sống và xã hội xung quanh.</p>
                <p>C.	Biết được các tin tức và sự kiện xảy ra trong xã hội.</p>
                <p>D.	Tất cả các khẳng định trên đều đúng.</p> 
                <Link to='/admin/cau-hoi/cap-nhat' >Cập nhật câu hỏi</Link> */}
                <Navbar />

                <Switch>
                    <PrivateRoute exact path="/" component={BaiGiangIndex}/>
                    <PrivateRoute path="/bai-giang/:id" unique='id' component={BaiGiangDetail} />
                    <PrivateRoute path="/chuong/:id" unique='id' component={ChuongDetail} />
                    <PrivateRoute path="/tiet/:id" unique='id' component={TietDetail} />
                    <PrivateRoute path="/cau-hoi/:id" unique='id' component={CauHoiIndex} />
                    <PrivateRoute path="/kiem-tra/" unique='id' component={KetQuaIndex} />
                    <PrivateRoute path="/tim-kiem/" unique='id' component={TimKiem} />
                </Switch>
            </div>
        )
    }
}
