import { faExclamationTriangle, faSpinner } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React, { Component } from 'react'
import Notify from '../../../components/notify/Notify'
import AdminService from '../../../services/AdminService'
import { TruncateSharp } from '../../../components/helpers/FieldValidate'

export default class BaiGiangDeleteModal extends Component {
    state = {
        isDeleting: false,
        id: ''
    }

    constructor(props) {
        super(props)

        console.log(props)
        this.state = {
            id: this.props.form?.id,
            deleting: false
        }
    }

    render() {
        const DelBTN = <FontAwesomeIcon icon={faExclamationTriangle} />
        return (
            <div className='w-100'>
                <div className="w-50 border rounded p-2 bg-white mt-5 shadow mx-auto row">
                    <div className="col-12">
                        <h4>Xác nhận xóa <u>{TruncateSharp(this.state.id)}</u></h4>
                        <hr />
                    </div>
                    <div className="col-6 mb-5">
                        {!this.state.isDeleting &&  <span
                            className='btn btn-outline-danger'
                            onClick={() => this.setState({ isDeleting: true })}
                        >
                            {DelBTN} Xóa
                        </span>}
                        {this.state.isDeleting && <>
                            {!this.state.deleting && <span
                                className='btn btn-danger'
                                onClick={() => {
                                    this.setState({
                                        deleting: true
                                    })
                                    AdminService.deleteBaiGiang(this.props.form)
                                    .then(response => {
                                        if (response?.status === 200) {
                                            Notify.warn('Đã xóa thành công 1 chương trình!')
                                            this.props.refresh()
                                        }
                                    })
                                }}
                            >
                                {DelBTN} XÁC NHẬN XÓA {DelBTN}
                            </span>}
                            {this.state.deleting && <span className='btn btn-danger'><FontAwesomeIcon icon={faSpinner} className='fa-spin' />   </span>}
                            <span className='text-danger' > dữ liệu sẽ bị xóa vĩnh viễn </span>
                        </>}
                    </div>
                    <div className="col-6 text-end text-right">
                        <span
                            className='btn btn-outline-success'
                            onClick={() => this.props.close()}
                        >
                            Hủy bỏ
                        </span>
                    </div>
                </div>
            </div>
        )
    }
}
