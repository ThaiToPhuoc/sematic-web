import React, { Component } from 'react'
import { Field, Form } from 'react-final-form'
import { Alphabetical, FieldNumberOnly } from '../../components/helpers/FieldValidate';
import { FieldArray } from "react-final-form-arrays";

import arrayMutators from "final-form-arrays";
import PropTypes from 'prop-types';
import Select from 'react-select'

import '../cauhoi/Style.scss'
import PublicService from '../../services/PublicService';
import Notify from '../../components/notify/Notify';

const SelectAdapter = ({ input, ...rest }) => (
    <Select {...input} {...rest} isSearchable={true} />
)

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
        PublicService.saveCauHoi(values)
        .then(response => {
            if (response?.status === 200) {
                this.setState({
                    form: response.data
                })
                Notify.success('Cập nhật câu hỏi thành công!');
            }
        }) 
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
                            <label htmlFor='cauHoi.noiDungCauHoi'>Nội dung câu hỏi</label>
                            <Field 
                                className='m-auto'
                                name='cauHoi.NoiDungCauHoi'
                                component='input'
                                type='text'
                                placeholder='...'
                            />
                        </div>
                        <div>
                            <label htmlFor='cauHoi.sttcauHoi'>Số thứ tự</label>
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
                                                name={`${dapAn}.ketQua`}
                                                type="checkbox"
                                            />
                                        </div>
                                        <span class="input-group-text">{Alphabetical(index + 1)}</span>
                                        <Field
                                            className='form-control'
                                            name={`${dapAn}.noiDungDapAn`}
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
                        <button type='submit'>Lưu</button>
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