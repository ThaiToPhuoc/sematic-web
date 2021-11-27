import React, { Component } from 'react'
import { Field, Form } from 'react-final-form';
import { Link } from 'react-router-dom';
import { Required } from '../../../components/helpers/FieldValidate';
import Notify from '../../../components/notify/Notify';
import UserService from '../../../services/UserService';

export default class DangNhap extends Component {

    submit = (values) => {

        UserService.login(values)
        .then(user => {
            if (user?.token) {
                Notify.info(`Welcome ${user.username}`);

                if (user.roles.includes('ROLE_ADMIN')) {
                    window.location.href = '/admin'
                } else {
                    this.props.history.push('/')
                }
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
                            <Field name="username" validate={Required}>
                                {({ input, meta }) => (
                                <div>
                                    <label>Tài khoản</label>
                                    <input {...input} type="text" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                                )}
                            </Field>
                            <Field name="password" validate={Required}>
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
                                value='đăng nhập'
                            />

                            <Link
                                to='/dang-ky'
                                className='float-end'
                            >
                                đăng ký
                            </Link>
                        </form>
                    )}
                />
            </div>
        )
    }
}
