import React, { Component } from 'react'
import { columns } from './TableConfig'

import ReactModal from 'react-modal'
import DataTable from 'react-data-table-component'
import AdminService from '../../../services/AdminService'
import { Height } from '../Components/Window90'
import BaiGiangCreate from './BaiGiangCreate'

import './style.scss'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faList, faPlusCircle, faPlusSquare } from '@fortawesome/free-solid-svg-icons'
import { TruncateSharp } from '../../../components/helpers/FieldValidate'
import ChuongModal from './Chuong/ChuongModal'
import KiemTraModal from './KiemTra/KiemTraModal'

const modalStyle = {
    content: {
        overflow: 'hidden',
        border: 'none',
        background: 'transparent'
    }
}

export default class BaiGiangList extends Component {
    constructor(props) {
        super(props)

        let _columns = [...columns.bai_giang]
        _columns.push({
            name: (
                <h4>
                    <FontAwesomeIcon 
                        icon={faPlusSquare}
                        className='rounded text-success pointer'
                        onClick={this.openCreateModal}
                    />
                </h4>
            ),
            selector: row => (
                <h5 className='m-0' onClick={() => this.select(row)}>
                    <FontAwesomeIcon 
                        icon={faList}
                        className='rounded text-info pointer'
                        fixedWidth
                    />
                </h5>
            ),
        })

        this.state = {
            columns: _columns,
            baiGiangs: [],
            height90: `${Height*95/100}px`,
            mCreateVisible: false,
            mChuongModal: false,
            mKiemTraModal: false,
            cacChuong: [],
            cacKiemTra: [],
            currChuong: { },
            currKiemTra: { },
        }
    }

    openCreateModal = () => {
        this.setState({
            mCreateVisible: true
        })
    }

    openChuongModal = (chuong) => {
        this.setState({
            mChuongModal: true,
            currChuong: chuong
        })
    }

    refresh = () => {
        this.setState({
            mChuongModal: false,
            mCreateVisible: false,
            mKiemTraModal: false,
            cacChuong: [],
            currChuong: {},
            currKiemTra: { },
        })
        AdminService.findAllBaiGiang()
        .then(response => {
            if (response?.data) {

                this.setState({
                    baiGiangs: response.data
                })
            }
        })
    }

    select = (item) => {
        let { baiGiangs } = this.state
        baiGiangs.forEach(baiGiang => {
            baiGiang.selected = baiGiang.id === item.id
        })

        this.setState({
            baiGiangs: baiGiangs,
            cacChuong: baiGiangs.find(bg => bg.id === item.id).gomChuong,

        })
        
        AdminService.findKiemTraByBaiGiang(TruncateSharp(item.id))
        .then(response => {
            if (response?.data) {
                this.setState({
                    cacKiemTra: response.data
                })
            }
        }) 
    }

    componentDidMount() {
        ReactModal.setAppElement('#root')
        this.refresh()
    }

    render() {
        const { height90 } = this.state;
        return (
            <>
            <div className='row'>
                <div className="col-lg-7">
                    <div
                        className='border rounded'
                        style={{
                            height: height90,
                            maxHeight: height90
                        }}
                    >
                        <DataTable
                            conditionalRowStyles={[
                                {
                                    when: row => row.selected,
                                    style: {
                                        backgroundColor: 'wheat'
                                    }
                                }
                            ]}
                            columns={this.state.columns}
                            data={this.state.baiGiangs}
                            fixedHeader={true}
                            fixedHeaderScrollHeight={height90}
                        />
                    </div>
                </div>

                <div className='col-lg-5'>
                    <div
                        className='d-flex flex-column'
                        style={{
                            height: height90,
                            maxHeight: height90
                        }}
                    >
                        <div className='border rounded h-50 mb-1 p-1'>
                        {
                            this.state.baiGiangs.some(bg => bg.selected)
                            ? (
                                <div className='d-flex flex-wrap'>
                                    {this.state.cacChuong
                                    ?.sort((a, b) => a.id > b.id ? 1 : -1)
                                    ?.map(chuong => {
                                        let id = TruncateSharp(chuong.id)
                                        return (
                                            <div key={id} className='border rounded px-1 mx-1 flex pointer'
                                                onClick={() => this.openChuongModal(chuong)}
                                            >
                                                {id}
                                            </div>
                                        )
                                    })}
                                    
                                    <div 
                                        className='border rounded px-1 mx-1 flex pointer'
                                        onClick={() => this.openChuongModal({  })}
                                    >
                                        <FontAwesomeIcon className='text-success' icon={faPlusCircle} />
                                        &nbsp;Thêm chương
                                    </div>
                                </div>
                            )
                            : ( /* GomChuong === undefind | null | empty */
                                <div className='w-100 h-100 position-relative'>
                                    <div
                                        style={{
                                            position: 'absolute',
                                            top: '50%',
                                            left: '50%',
                                            transform: 'translate(-50%, 0%)'
                                        }}
                                    >
                                        <h5>Không tìm thấy chương liên quan!</h5>
                                    </div>
                                </div>
                            )
                        }
                        </div>

                        <div className='border rounded h-50 mt-1 p-1'>
                        {
                            this.state.baiGiangs.some(bg => bg.selected)
                            ? (
                                <div className='d-flex flex-wrap'>
                                    {this.state.cacKiemTra
                                    .sort((a, b) => a.kiemTra.id > b.kiemTra.id ? 1 : -1)
                                    .map(kiemTraDTO => {
                                        let id = TruncateSharp(kiemTraDTO?.kiemTra?.id)
                                        return (
                                            <div key={id} className='border rounded px-1 mx-1 flex pointer'
                                                onClick={() => this.setState({ mKiemTraModal: true, currKiemTra: kiemTraDTO })}
                                            >
                                                {id}
                                            </div>
                                        )
                                    })}
                                    
                                    <div 
                                        className='border rounded px-1 mx-1 flex pointer'
                                        onClick={() => this.setState({ mKiemTraModal: true, currKiemTra: { } })}
                                    >
                                        <FontAwesomeIcon className='text-success' icon={faPlusCircle} />
                                        &nbsp;Thêm kiểm tra
                                    </div>
                                </div>
                            )
                            : ( /* GomChuong === undefind | null | empty */
                                <div className='w-100 h-100 position-relative'>
                                    <div
                                        style={{
                                            position: 'absolute',
                                            top: '50%',
                                            left: '50%',
                                            transform: 'translate(-50%, 0%)'
                                        }}
                                    >
                                        <h5>Không tìm thấy Kiểm tra liên quan!</h5>
                                    </div>
                                </div>
                            )
                        }
                        </div>
                    </div>
                </div>
            </div>
            
            <ReactModal 
                style={modalStyle}
                isOpen={this.state.mCreateVisible}>
                <BaiGiangCreate
                    close={() => this.setState({ mCreateVisible: false })}
                    refresh={this.refresh}
                />
            </ReactModal>

            <ReactModal
                style={modalStyle}
                isOpen={this.state.mChuongModal}>
            >
                <ChuongModal 
                    readMode={true}
                    form={this.state.currChuong}
                    thuocBaiGiang={this.state.baiGiangs.find(bg => bg.selected)}
                    close={() => this.setState({ mChuongModal: false, currChuong: { } })}
                    refresh={this.refresh}
                />
            </ReactModal>

            <ReactModal
                style={modalStyle}
                isOpen={this.state.mKiemTraModal}>
            >
                <KiemTraModal 
                    readMode={true}
                    form={this.state.currKiemTra}
                    thuocBaiGiang={this.state.baiGiangs.find(bg => bg.selected)}
                    close={() => this.setState({ mKiemTraModal: false, currKiemTra: { } })}
                    refresh={this.refresh}
                />
            </ReactModal>
            </>
        )
    }
}