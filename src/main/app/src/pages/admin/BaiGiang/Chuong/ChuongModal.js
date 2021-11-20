import React, { Component } from 'react'
import { Field, Form } from 'react-final-form';
import Select from 'react-select/dist/declarations/src/Select';
import { Required } from '../../../components/helpers/FieldValidate'


const SelectAdapter = ({ input, ...rest }) => (
    <Select {...input} {...rest} isSearchable={true} />
)

export default class ChuongModal extends Component {

    onSubmit = (values, form) => {
        window.alert(JSON.stringify(values, 0, 2))
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
                        <Field name="sttchuong" validate={Required}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Số chương</label>
                                    <input {...input} type="number" />
                                    {meta.error && meta.touched && <div className='text-danger'>{meta.error}</div>}
                                </div>
                            )}
                        </Field>
                        
                        <Field name="noiDungChuong" validate={Required}>
                            {({ input, meta }) => (
                                <div>
                                    <label>Nội dung chương</label>
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
