import React, { Component } from 'react'
import { Route, Switch } from 'react-router';
import { Link } from 'react-router-dom';
import Notify from '../../components/notify/Notify';
import CauHoiUpdate from '../cauhoi/CauHoiUpdate';
import CauHoiIndex from './CauHoiIndex';
import ChuongDetail from './ChuongDetail';

export default class Index extends Component {
    componentDidMount() {
        Notify.info('Xin chào!');
    }

    render() {
        return (
            <div>
                <CauHoiIndex />
                {/* <h2 class="sub-title">Trắc nghiệm Tin học lớp 6:</h2>
                <p><b class="color-green">Bài 1:</b> Thông tin có thể giúp cho con người:</p>
                <p>A.	Nắm được quy luật của tự nhiên và do đó trở nên mạnh mẽ hơn.</p>
                <p>B.	Hiểu biết về cuộc sống và xã hội xung quanh.</p>
                <p>C.	Biết được các tin tức và sự kiện xảy ra trong xã hội.</p>
                <p>D.	Tất cả các khẳng định trên đều đúng.</p> */}

                <Link to='/admin/cau-hoi/cap-nhat' >Cập nhật câu hỏi</Link>
                <Switch>
                    <Route exact path="/" component={CauHoiIndex}/>
                    <Route exact path="/chuong/:id" component={ChuongDetail} />
                    <Route exact path='/admin/cau-hoi/cap-nhat' component={CauHoiUpdate} />
                </Switch>
            </div>
        )
    }
}
