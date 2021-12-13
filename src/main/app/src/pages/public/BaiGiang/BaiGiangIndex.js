import React, { Component } from 'react'
import Notify, { AlertTypes } from '../../../components/notify/Notify';
import {Alphabetical, TruncateSharp} from '../../../components/helpers/FieldValidate';
import PublicService from '../../../services/PublicService';
import { Link } from 'react-router-dom';
import Accordion from '../../../components/Accordion/Accordion';

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
                this.setState({
                    BaiGiang: response.data
                })
            }
        })
        .catch((error) => {
            Notify.error(error.message)
        })
    }

    getTietId = (tiet) => {
        let id = tiet?.id;
        id = id.substring(id.indexOf('#') + 1)
        return <Link to={`/tiet/${id}`}>{tiet.link}</Link>
    }

    render() {
        return (
            <div>
                <div class = 'container'>
                    <h2 class="text-center">WEB NGỮ NGHĨA XÂY DỰNG HỆ THỐNG TRỢ GIÚP HỌC TẬP CHO HỌC SINH THCS LAM SƠN ĐÀ LẠT</h2>
                    <p>
                        So với các ngành công nghiệp khác, ngành công nghiệp Công nghệ thông tin 
                        trong những năm gần đây đã phát triển nhanh chóng và đã thúc đẩy sự phát triển 
                        nhiều lĩnh vực khác như: Y học, giáo dục, kinh tế - chính trị, văn hóa - xã hội, nghiên cứu, giải trí,… 
                        Lợi ích mà nó mang lại cho con người là rất lớn. 
                        Đặc biệt các thành tựu nổi bật nhất của công nghệ thông tin trong giáo dục và đào tạo hiện nay chính là 
                        dạy học trên Website. Điều này đã nâng cao được khả năng tự học của học sinh và dường như nó đã trở thành 
                        cầu nối giữa giáo viên, gia đình, nhà trường và học sinh.
                    </p>
                    <p>
                        Ứng dụng web ngữ nghĩa xây dựng hệ thống trợ giúp học tập cho 
                        học sinh THCS Lam Sơn Đà Lạt” nhằm góp phần nâng cao khả năng tự học, 
                        khả năng sáng tạo, các em tìm ra phương pháp học tập hợp lý cho bản thân và 
                        đồng thời rèn luyện, vận dụng kiến thức môn học trong trường phổ thông vào thực tiễn hợp lý và hiệu quả.
                    </p>
                    
                </div>
                <div className='container'>
                    <h2 class="text-center">Danh sách các chương trình</h2>
                    {
                        this.state.BaiGiang.map((bg) =>(
                                <Accordion title={`Chương trình lớp ${bg.chuongTrinh} - Học kỳ ${bg.hocKy}`}>
                                    <div className='row gx-3'>
                                    {bg.gomChuong
                                    ?.sort((a, b) => a.sttchuong > b.sttchuong ? 1 : -1)
                                    ?.map((chuong) => (
                                            <div className='col-3'>
                                                <div className='p-2'>
                                                    <h6>
                                                        <Link className='text-dark' to={`/chuong/${TruncateSharp(chuong.id)}`}>
                                                            Chương {chuong.sttchuong}: {chuong.noiDungChuong}
                                                        </Link>
                                                    </h6>

                                                    <ol>
                                                    {chuong.gomTiet?.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
                                                    .map((tiet) => (
                                                        <li>
                                                            <Link to={`/tiet/${TruncateSharp(tiet.id)}`}>{tiet.link}  </Link>
                                                        </li>
                                                    ))}
                                                    </ol>
                                                </div>
                                            </div>
                                        ))
                                    }
                                    </div>
                                </Accordion>
                            )
                        )
                    }   
                    
                </div>
            </div>
        )
    }
}
