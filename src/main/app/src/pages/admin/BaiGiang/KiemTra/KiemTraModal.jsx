import React, { Component } from 'react'
import { Field, Form } from 'react-final-form';
import { FieldArray } from 'react-final-form-arrays';
import arrayMutators from "final-form-arrays";
import { Required, Alphabetical } from '../../../../components/helpers/FieldValidate';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlusSquare, faTimes } from '@fortawesome/free-solid-svg-icons';
import AdminService from '../../../../services/AdminService';
import Notify from '../../../../components/notify/Notify';

export default class KiemTraModal extends Component {
    constructor(props) {
        super(props)

        if (!props.thuocBaiGiang) {
            throw new Error('ChuongModal required attribute "thuocBaiGiang"');
        }

        let formIsEmpty = !props.form 
                            || (props.form
                            && Object.keys(props.form).length === 0
                            && Object.getPrototypeOf(props.form) === Object.prototype)

        this.state = {
            isReadMode: props.readMode  && !formIsEmpty ? true : false,
            createMode: formIsEmpty
        }
    }

    onSubmit = (values, form) => {
        let apiCall = this.state.createMode ? AdminService.createKiemTra(values) : AdminService.updateKiemTra(values)

        apiCall.then(response => {
            if (response?.status === 200) {
                Notify.success(`${this.state.createMode ? 'Tạo' : 'Cập nhật'} thành công 1 Bài kiểm tra!`)
                this.props.refresh()
                this.onClose(form)
            }
        })
    }

    onClose = (form) => {
        form.reset()
        this.props.close()
    }

    sortFieldArray = (fields) => {
        if (!fields?.value) {
            return []
        } else {
            fields.value.sort((a, b) => a.cauHoi.sttcauHoi > b.cauHoi.sttcauHoi ? 1 : -1)
    
            return fields
        }
    }

    render() {
        return (
            <Form 
                onSubmit={this.onSubmit}
                mutators={{
                    ...arrayMutators
                }}
                initialValues={this.props.form}
                render={({ handleSubmit, form, values }) => (
                    <form className='bg-white' style={{maxWidth: '1000px'}} onSubmit={handleSubmit}>
                        <Field 
                            component='input'
                            type='hidden'
                            name='kiemTra.thuocBaiGiang.id'
                            initialValue={this.props.thuocBaiGiang.id}
                        />

                        <FieldArray name='cauHoi'>
                            {({ fields }) => (
                                <div className='d-flex flex-column px-2'>
                                    <label htmlFor='cauHoi'>
                                        <h4>Các câu hỏi</h4>
                                    </label>
                                    <hr />
                                    {this.sortFieldArray(fields)
                                    .map((tiet, index) => (
                                        <div key={`T_${index}`} className='border border-dark rounded p-1 my-3'>
                                            <Field 
                                                component='input'
                                                type='hidden'
                                                name='thuocChuong.id'
                                                initialValue={values.id}
                                            />
                                            <div key={tiet} className='input-group'>
                                                <span className="input-group-text">Câu</span>
                                                <Field
                                                    initialValue={index + 1}
                                                    className='form-control'
                                                    component="input"
                                                    name={`${tiet}.cauHoi.sttcauHoi`}
                                                    type="number"
                                                    placeholder='STT Câu hỏi...'
                                                    disabled={this.state.isReadMode}
                                                    readOnly={this.state.isReadMode} 
                                                />

                                                <Field
                                                    className='form-control w-75'
                                                    name={`${tiet}.cauHoi.tuKhoa`}
                                                    component="input"
                                                    placeholder='Từ khóa...'
                                                    disabled={this.state.isReadMode}
                                                    readOnly={this.state.isReadMode} 
                                                />

                                                {!this.state.isReadMode ?
                                                <div class="input-group-append">
                                                    <span
                                                        className="input-group-text"
                                                        onClick={() => fields.remove(index)}
                                                        style={{ cursor: 'pointer' }}
                                                    >
                                                        <FontAwesomeIcon className='fs-4 text-danger' icon={faTimes} />
                                                    </span>
                                                </div>
                                                :
                                                <></>
                                                }
                                            </div>
                                            

                                            <Field
                                                className='form-control'
                                                name={`${tiet}.cauHoi.noiDungCauHoi`}
                                                component="textarea"
                                                placeholder='Nội dung câu hỏi...'
                                                disabled={this.state.isReadMode}
                                                readOnly={this.state.isReadMode} 
                                            />

                                            <div className='mt-3 mb-1'><b>Đáp án: </b></div>
                                            <FieldArray name={`cauHoi[${index}].dapAns`}>
                                                {({ fields }) => (
                                                    <div className='p-2 border rounded bg-light'>
                                                        {fields.map((daField, indexChild) => (
                                                            <div key={`DA_${index}_${indexChild}`} className='mb-3'>
                                                                <div className='d-flex'>
                                                                    <div>
                                                                        Đáp án <b>{Alphabetical(indexChild + 1)}</b>
                                                                    </div>
                                                                    <span className='mx-2'> | </span>
                                                                    <div className='form-check form-switch'>
                                                                        <label className="form-check-label" htmlFor={`${daField}.ketQua`}>Kết quả</label>
                                                                        <Field
                                                                            className="form-check-input"
                                                                            component="input"
                                                                            type="checkbox"
                                                                            name={`${daField}.ketQua`}
                                                                            parse={v => v ? 1 : 0}
                                                                            disabled={this.state.isReadMode}
                                                                            readOnly={this.state.isReadMode} 
                                                                        />
                                                                    </div>
                                                                    <div className='ms-auto'>
                                                                        {!this.state.isReadMode ?
                                                                        <span
                                                                            onClick={() => fields.remove(index)}
                                                                            style={{ cursor: 'pointer' }}
                                                                        >
                                                                            <FontAwesomeIcon className='fs-4 text-warning' icon={faTimes} />
                                                                        </span>
                                                                        : <></>
                                                                        }
                                                                    </div>
                                                                </div>
                                                                <div className='input-group'>
                                                                    <Field
                                                                        className='form-control'
                                                                        component="textarea"
                                                                        name={`${daField}.noiDungDapAn`}
                                                                        placeholder='Nội dung đáp án...'
                                                                        disabled={this.state.isReadMode}
                                                                        readOnly={this.state.isReadMode} 
                                                                    />
                                                                </div>
                                                            </div>
                                                        ))}
                                                        {!this.state.isReadMode ? 
                                                        <div className='text-end'>
                                                            <FontAwesomeIcon 
                                                                className='fs-4 text-info pointer' 
                                                                icon={faPlusSquare} 
                                                                onClick={() => form.mutators.push(`cauHoi[${index}].dapAns`, { 
                                                                    ketQua: 0,
                                                                    noiDungDapAn: ""
                                                                })}
                                                            />
                                                        </div>
                                                        : <></>
                                                        }
                                                    </div>
                                                )}
                                            </FieldArray>
                                        </div>
                                    ))}
                                    {!this.state.isReadMode ?
                                    <div className='text-end'>
                                        <FontAwesomeIcon 
                                            className='fs-4 text-success pointer' 
                                            icon={faPlusSquare} 
                                            onClick={() => form.mutators.push('cauHoi', { 
                                                cauHoi: { },
                                                dapAns: []
                                            })}
                                        />
                                    </div>
                                    : <></>
                                    }
                                </div>
                            )}
                        </FieldArray>
                        
                        <hr />
                        <div className='row'>
                            <div className='col-md-12 px-0 text-end text-right'>
                                {this.state.isReadMode ?
                                <span 
                                    className='btn btn-warning me-1' 
                                    onClick={() => this.setState(prev => ({ isReadMode: !prev.isReadMode }))}
                                >
                                    Sửa
                                </span>
                                :
                                <button className='btn btn-success me-1' type='submit'>
                                    Lưu
                                </button>
                                }

                                <button className='btn btn-danger' onClick={() => this.onClose(form)}>
                                    Hủy
                                </button>
                            </div>
                        </div>
                    </form>
                )}
            />
        )
    }
}
