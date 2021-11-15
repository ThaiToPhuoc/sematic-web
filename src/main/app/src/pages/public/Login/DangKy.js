import React, { Component } from 'react'
import { Field, Form } from 'react-final-form'
import { Required } from '../../../components/helpers/FieldValidate'
import Notify from '../../../components/notify/Notify';
import UserService from '../../../services/UserService';

export default class DangKy extends Component {

    submit = (values) => {
        console.log(values);

        UserService.register(values)
        .then(response => {
            if (response?.status === 200) {
                Notify.success("Đăng ký thành công!")

                this.props.history.push("/dang-nhap")
            }
        })
    }
    render() {
        return (
            <div>
                <Form 
                    onSubmit={this.submit}
                    render={({ handleSubmit, form, values }) => (
                        <form onSubmit={handleSubmit}>
                            <Field name="tenTaiKhoan" validate={Required}>
                                {({ input, meta }) => (
                                <div>
                                    <label>Tài khoản</label>
                                    <input {...input} type="text" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                                )}
                            </Field>
                            <Field name="matKhau" validate={Required}>
                                {({ input, meta }) => (
                                <div>
                                <label>Mật khẩu</label>
                                    <input {...input} type="password" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                                )}
                            </Field>

                            <input
                                type='submit'
                                className='btn btn-outline-primary'
                                value='đăng ký'
                            />
                        </form>
                    )}
                />
            </div>
        )
    }
}
