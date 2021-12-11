import { faSpinner } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React, { Component } from 'react'
import OutsideClickHandler from 'react-outside-click-handler'
import { Link } from 'react-router-dom'
import PublicService from '../../services/PublicService'

const Map = {
    baigiang: {
        path: '/bai-giang',
        label: 'Bài Giảng'
    },
    chuong: {
        path: '/chuong',
        label: 'Chương'
    },
    tiet: {
        path: '/tiet',
        label: 'Tiết'
    },
    kiemtra: {
        path: '/cau-hoi',
        label: 'Kiểm Tra'
    },
}

export default class BasicSearch extends Component {
    constructor(props) {
        super(props)
        this.state = {
            text: '',
            timeout: 0,
            show: false,
            loading: false,
            results: []
        }
    }

    onChange = (event) => {
        this.setState({
           text: event.target.value
        }) 

        if(this.timeout) clearTimeout(this.timeout);

        this.timeout = setTimeout(() => {
            if (this.state.text) {
                this.search()
            } else {
                this.setState({
                    results: []
                })
            }
        }, 500);
    }

    search = () => {
        this.setState({
            loading: true
        })

        PublicService.searchBasic({
            text: this.state.text
        })
        .then(response => {
            console.log(response)
            this.setState({
                results: response?.data
            })
        })
        .finally(() => this.setState({
            loading: false
        }))
    }

    render() {
        const NF = <div className='text-center py-1'>
            Không có kết quả
        </div>
        return (
            <div className='position-relative'>
                <OutsideClickHandler onOutsideClick={() => this.setState({ show: false })}>
                    <textarea
                        className='w-100 form-control'
                        onFocus={() => this.setState({ show: true })}
                        onChange={this.onChange}
                        placeholder='Tìm kiếm...'
                        rows={1}
                    ></textarea>

                    {this.state.show && <div 
                        className='position-absolute border border-dark rounded bg-white shadow overflow-hidden'
                        style={{ left: '0', width: '100%', top: 'inherit', }}
                    >
                        {
                            this.state.loading ?
                            <div className='text-center'>
                                <FontAwesomeIcon className='fa-pulse' icon={faSpinner} />
                            </div>
                            :
                            !this.state.results.every(r => r?.results?.length === 0) ? 
                            this.state.results
                            .filter(r => r?.results?.length)
                            .map((result, index, arr) => (
                                <div className='px-2' key={index}>
                                    <b>{Map[result.type].label}</b>
                                    <ul>
                                        {result.results.map(({ url, value }) => (
                                            <li key={url}>
                                                <Link to={`${Map[result.type].path}/${url}`}>
                                                    {value}
                                                </Link>
                                            </li>
                                        ))}
                                    </ul>
                                    {arr.length > index + 1 && <hr className='mb-0' />}
                                </div>
                            )) :
                            <>{NF}</>
                        }
                    </div>}
                </OutsideClickHandler>
            </div>
        )
    }
}
