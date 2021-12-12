import React, { Component } from 'react'
import { columns } from './TableConfig'

import ReactModal from 'react-modal'
import DataTable from 'react-data-table-component'
import AdminService from '../../../services/AdminService'
import { Height } from '../Components/Window90'
import BaiGiangCreate from './BaiGiangCreate'

import './style.scss'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faFileVideo, faList, faPlusCircle, faPlusSquare, faTimes } from '@fortawesome/free-solid-svg-icons'
import { TruncateSharp } from '../../../components/helpers/FieldValidate'
import ChuongModal from './Chuong/ChuongModal'
import KiemTraModal from './KiemTra/KiemTraModal'
import VideoModal from './Media/VideoModal'
import BaiGiangDeleteModal from './BaiGiangDeleteModal'

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
        _columns.push(
            {
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
            },
            {
                name: '',
                selector: row => (
                    <span className='rounded btn btn-outline-danger px-1' onClick={() => {
                        this.select(row)
                        this.setState({
                            mBGDelModal: true
                        })
                    }}>
                        <FontAwesomeIcon 
                            icon={faTimes}
                            fixedWidth
                        /> <small className='me-1'>Xóa</small>
                    </span>
                ),
            }
        )

        this.state = {
            columns: _columns,
            baiGiangs: [],
            height90: `${Height*95/100}px`,
            mCreateVisible: false,
            mChuongModal: false,
            mKiemTraModal: false,
            mVideoModal: false,
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

    openChuongOption = (chuong) => {
        this.setState(prev => ({
            currChuong: prev.currChuong?.id === chuong.id ? { } : chuong
        }))
    }

    refresh = () => {
        this.setState({
            mChuongModal: false,
            mCreateVisible: false,
            mKiemTraModal: false,
            mBGDelModal: false,
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

    openVideo = (item) => {
        this.select(item)
        this.setState({
            mVideoModal: true
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
                                            <div key={id} className='border rounded px-1 mx-1 flex position-relative noselect'   
                                            >
                                                {id}
                                                <div style={{
                                                        left: '50%',
                                                        top: '0',
                                                        width: '100%',
                                                        height: '100%',
                                                        position: 'absolute',
                                                        transform: 'translate(-50%, 0)',
                                                }} className='pointer' onClick={() => this.openChuongOption(chuong)}></div>

                                                {(!this.state.mChuongModal 
                                                && !this.state.mVideoModal
                                                && this.state.currChuong 
                                                && this.state.currChuong.id === chuong.id
                                                && <div style={{
                                                        left: '50%',
                                                        bottom: '-5px',
                                                        width: '100%',
                                                        position: 'absolute',
                                                        backgroundColor: 'white',
                                                        border: '1px solid black',
                                                        transform: 'translate(-50%, 100%)',
                                                    }}
                                                    className='d-flex flex-column'
                                                >
                                                    <div 
                                                        className='border-bottom px-2 c-option'
                                                        onClick={() => this.openChuongModal(chuong)}
                                                    >Chi tiết</div>

                                                    <div 
                                                        className='px-2 c-option'
                                                        onClick={() => this.setState({ mVideoModal: true })}
                                                    >Video</div>
                                                </div>)}
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

            <ReactModal
                style={modalStyle}
                isOpen={this.state.mVideoModal}
            >
                <VideoModal 
                    id={this.state.currChuong?.id}
                    close={() => this.setState({ mVideoModal: false, currChuong: { } })}
                />
            </ReactModal>

            <ReactModal
                style={modalStyle}
                isOpen={this.state.mBGDelModal}
            >
                <BaiGiangDeleteModal 
                    form={this.state.baiGiangs.find(bg => bg.selected)}
                    close={() => this.setState({ mBGDelModal: false })}
                    refresh={this.refresh}
                />
            </ReactModal>
            </>
        )
    }
}