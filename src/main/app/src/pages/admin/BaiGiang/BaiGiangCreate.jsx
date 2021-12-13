import React, { Component } from 'react'
import { Field, Form } from 'react-final-form';
import { Required } from '../../../components/helpers/FieldValidate'
import Notify from '../../../components/notify/Notify';
import AdminService from '../../../services/AdminService';

export default class BaiGiangCreate extends Component {

    onSubmit = (values, form) => {
        AdminService.createBaiGiang(values)
        .then(response => {
            if (response?.status === 200) {
                Notify.success('Tạo thành công 1 Chương trình!')
                this.props.refresh()
                this.onClose(form)
            }
        })
    }

    onClose = (form) => {
        form.reset()
        this.props.close()
    }

    render() {
        return (
            <Form 
                onSubmit={this.onSubmit}
                render={({ handleSubmit, form, values }) => (
                    <form onSubmit={handleSubmit}>
                        <Field name="chuongTrinh" validate={Required}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Chương trình</label>
                                    <input {...input} type="number" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>
                        
                        <Field name="hocKy" validate={Required}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Học kỳ</label>
                                    <input {...input} type="number" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>

                        <Field name="namHoc">
                            {({ input, meta }) => (
                                <div>
                                    <label>Năm học</label>
                                    <input {...input} type="number" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>

                        <div className='row'>
                            <div className='col-md-12 px-0 text-end text-right'>
                                <button className='btn btn-success me-1' type='submit'>
                                    Lưu
                                </button>

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