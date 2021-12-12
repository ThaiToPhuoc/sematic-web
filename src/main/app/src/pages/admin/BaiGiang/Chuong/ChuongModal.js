import React, { Component } from 'react'
import { Field, Form } from 'react-final-form';
import { FieldArray } from 'react-final-form-arrays';
import Select from 'react-select';
import arrayMutators from "final-form-arrays";
import { Required } from '../../../../components/helpers/FieldValidate';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExclamationTriangle, faPlusSquare, faTimes } from '@fortawesome/free-solid-svg-icons';
import AdminService from '../../../../services/AdminService';
import Notify from '../../../../components/notify/Notify';
import ReactQuill, { Quill } from 'react-quill'
import ImageResize from 'quill-image-resize-module-react';


const SelectAdapter = ({ input, ...rest }) => (
    <Select {...input} {...rest} isSearchable={true} />
)

Quill.register('modules/imageResize', ImageResize);

export default class ChuongModal extends Component {
    modules = {  
        toolbar: {  
            container: [  
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }],  
                ['bold', 'italic', 'underline'],  
                [{ 'list': 'ordered' }, { 'list': 'bullet' }],  
                [{ 'align': [] }],  
                ['link', 'image'],  
                ['clean'],  
                [{ 'color': [] }]  
            ]
        },  
        imageResize: { 
            modules: [ 'Resize', 'DisplaySize', 'Toolbar' ]
        }
    }
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
            isReadMode: props.readMode && !formIsEmpty ? true : false,
            createMode: formIsEmpty,
            isDeleting: false
        }
    }

    onSubmit = (values, form) => {
        if (this.state.createMode) {
            AdminService.createChuong(values)
            .then(response => {
                if (response?.status === 200) {
                    Notify.success('Tạo thành công 1 Chương!')
                    this.props.refresh()
                    this.onClose(form)
                } else {
                    Notify.error('Đã có lỗi xảy ra')
                }
            })
        } else {
            AdminService.updateChuong(values)
            .then(response => {
                if (response?.status === 200) {
                    Notify.success('Cập nhật thành công 1 Chương!')
                    this.props.refresh()
                    this.onClose(form)
                } else {
                    Notify.error('Đã có lỗi xảy ra')
                }
            })
        }
    }

    onClose = (form) => {
        form.reset()
        this.props.close()
    }

    sortFieldArray = (fields) => {
        if (!fields?.value) {
            return []
        } else {
            fields.value.sort((a, b) => a.stttiet > b.stttiet ? 1 : -1)
    
            return fields
        }
    }

    render() {
        const DelBTN = <FontAwesomeIcon icon={faExclamationTriangle} />
        return (
            <Form 
                onSubmit={this.onSubmit}
                mutators={{
                    ...arrayMutators
                }}
                initialValues={this.props.form}
                render={({ handleSubmit, form, values }) => (
                    <form style={{maxWidth: '1000px'}} onSubmit={handleSubmit}>
                        <Field 
                            component='input'
                            type='hidden'
                            name='thuocBaiGiang.id'
                            initialValue={this.props.thuocBaiGiang.id}
                        />
                        <Field name="sttchuong" validate={(val) => Required(val)}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Số chương</label>
                                    <input {...input} 
                                        type="number" 
                                        disabled={this.state.isReadMode}
                                        readOnly={this.state.isReadMode} 
                                    />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>
                        
                        <Field name="noiDungChuong" validate={Required}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Nội dung chương</label>
                                    <textarea {...input} 
                                        disabled={this.state.isReadMode}
                                        readOnly={this.state.isReadMode} 
                                    ></textarea>
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>
                        
                        <hr />
                        <FieldArray name='gomTiet'>
                            {({ fields }) => (
                                <div className='d-flex flex-column px-2'>
                                    <label htmlFor='gomTiet'>Gồm tiết</label>
                                    {this.sortFieldArray(fields)
                                    .map((tiet, index) => (
                                        <div key={`T_${index}`}>
                                            <Field 
                                                component='input'
                                                type='hidden'
                                                name='thuocChuong.id'
                                                initialValue={values.id}
                                            />
                                            <div key={tiet} className='input-group'>
                                                <Field
                                                    className='form-control'
                                                    component="input"
                                                    name={`${tiet}.stttiet`}
                                                    type="number"
                                                    placeholder='STT Tiết...'
                                                    disabled={this.state.isReadMode}
                                                    readOnly={this.state.isReadMode} 
                                                />
                                                {/* <span class="input-group-text">{Alphabetical(index + 1)}</span> */}
                                                <Field
                                                    className='form-control'
                                                    name={`${tiet}.tuKhoa`}
                                                    component="input"
                                                    placeholder='Từ khóa...'
                                                    disabled={this.state.isReadMode}
                                                    readOnly={this.state.isReadMode} 
                                                />

                                                <Field
                                                    className='form-control w-50'
                                                    name={`${tiet}.link`}
                                                    component="input"
                                                    placeholder='Tiêu đề...'
                                                    disabled={this.state.isReadMode}
                                                    readOnly={this.state.isReadMode} 
                                                />

                                                {!this.state.isReadMode ?
                                                <div className="input-group-append">
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
                                            {/* <Field
                                                className='form-control'
                                                name={`${tiet}.noiDungTiet`}
                                                component="textarea"
                                                placeholder='Nội dung tiết...'
                                                disabled={this.state.isReadMode}
                                                readOnly={this.state.isReadMode} 
                                            /> */}
                                            <Field name={`${tiet}.noiDungTiet`} validate={Required}>
                                                {({ input, meta }) => (
                                                    <ReactQuill 
                                                        modules={this.modules}  
                                                        className={this.state.isReadMode ? '' : 'bg-white'}
                                                        defaultValue={input.value ? input.value : ''}
                                                        disabled={this.state.isReadMode}
                                                        readOnly={this.state.isReadMode}
                                                        onChange={input.onChange}
                                                        onFocus={input.onFocus}
                                                        onBlur={input.onBlur}
                                                        style={{
                                                            zIndex: 99999,
                                                            clear: 'both',
                                                            zoom: 1
                                                        }}
                                                    />
                                                )}
                                            </Field>
                                            <div className='my-2' style={{borderTop: '2px dotted rgba(0, 0, 0, 0.1)'}}></div>
                                        </div>
                                    ))}
                                    {!this.state.isReadMode ?
                                    <div className='text-end'>
                                        <FontAwesomeIcon 
                                            className='fs-4 text-success pointer' 
                                            icon={faPlusSquare} 
                                            onClick={() => form.mutators.push('gomTiet', { 
                                                stttiet: values.gomTiet ? values.gomTiet.length + 1 : 1
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
                            <div className='col-6 px-0 text-start text-left'>
                                {!this.state.isReadMode && 
                                !this.state.isDeleting && 
                                !this.state.createMode && <span
                                    className='btn btn-outline-danger'
                                    onClick={() => this.setState({ isDeleting: true })}
                                >
                                    {DelBTN} Xóa
                                </span>}

                                {!this.state.isReadMode && 
                                this.state.isDeleting && 
                                !this.state.createMode && <>
                                    <span
                                        className='btn btn-danger'
                                        onClick={() => {
                                            AdminService.deleteChuong(this.props.form)
                                            .then(response => {
                                                if (response?.status === 200) {
                                                    Notify.warn('Đã xóa thành công 1 chương!')
                                                    this.props.refresh()
                                                    this.onClose(form)
                                                }
                                            })
                                        }}
                                    >
                                        {DelBTN} XÁC NHẬN XÓA {DelBTN}
                                    </span>
                                    <span className='text-danger'> dữ liệu sẽ bị xóa vĩnh viễn </span>
                                </>}
                            </div>
                            <div className='col-6 px-0 text-end text-right'>
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
