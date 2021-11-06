import React, { Component } from 'react'
import { Field, Form } from 'react-final-form'
import { Alphabetical, FieldNumberOnly } from '../../components/helpers/FieldValidate';
import { FieldArray } from "react-final-form-arrays";

import arrayMutators from "final-form-arrays";
import PropTypes from 'prop-types';

import '../cauhoi/Style.scss'

export default class CauHoiUpdate extends Component {
    constructor(props) {
        super(props)
        let id = props.id ? props.id : '';
        this.state = {
            form: {
                cauHoi: {
                    id: id
                },
                dapAns: []
            }
        }
    }

    submit = (values) => {
    }

    render() {
        return (
            <div className='container'>
                <Form 
                    onSubmit={this.submit}
                    initialValues={this.state.form}
                    mutators={{
                        ...arrayMutators
                    }}
                    render={({ handleSubmit, form, submitting, pristine, values }) => (
                    <form onSubmit={handleSubmit} className='mx-auto'>
                        <h3>Cập nhật câu hỏi</h3>
                        <hr />
                        <div>
                            <label htmlFor='cauHoi.NoiDungCauHoi'>Nội dung câu hỏi</label>
                            <Field 
                                className='m-auto'
                                name='cauHoi.NoiDungCauHoi'
                                component='input'
                                type='text'
                                placeholder='...'
                            />
                        </div>
                        <div>
                            <label htmlFor='cauHoi.STTCauHoi'>Số thứ tự</label>
                            <Field 
                                className='m-auto'
                                name='cauHoi.STTCauHoi'
                                component='input'
                                type='text'
                                onInput={(e) => { e.target.value = FieldNumberOnly(e.target.value); } }
                                placeholder='...'
                            />
                        </div>
                        <hr />
                        <h5>Danh sách đáp án</h5>
                        <div className='flex-column'>
                            <FieldArray name="dapAns">
                                {({ fields }) =>
                                    fields.map((dapAn, index) => (
                                    <div key={dapAn} className='input-group'>
                                        <div className='input-group-text'>
                                            <Field
                                                component="input"
                                                name={`${dapAn}.KetQua`}
                                                type="checkbox"
                                            />
                                        </div>
                                        <span class="input-group-text">{Alphabetical(index + 1)}</span>
                                        <Field
                                            className='form-control'
                                            name={`${dapAn}.NoiDungDapAn`}
                                            component="input"
                                        />
                                        <div class="input-group-append">
                                            <span
                                                class="input-group-text"
                                                onClick={() => fields.remove(index)}
                                                style={{ cursor: 'pointer' }}
                                            >
                                            ❌
                                            </span>
                                        </div>
                                    </div>
                                    ))
                                }
                            </FieldArray>
                        </div>

                        <div className="text-end">
                            <button
                                className='btn btn-success'
                                type="button"
                                onClick={() => form.mutators.push('dapAns', { id: '' })}
                            >
                                ➕
                            </button>
                        </div>
                        <hr />
                        <pre>{JSON.stringify(values, 0, 2)}</pre>
                    </form>
                )}/>
            </div>
        )
    }
}

CauHoiUpdate.propTypes = {
    id: PropTypes.string
}