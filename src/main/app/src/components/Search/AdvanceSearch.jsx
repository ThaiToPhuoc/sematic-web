import { faPlus, faSearch, faTimes } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import PublicService from '../../services/PublicService'
import { LContainer, LLabel } from './LContainer'
import './style.scss'

interface LQuery {
    id: String;
    name: String;
    value: String | LQuery
}
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

export default class AdvanceSearch extends Component {
    state: { 
        queries: LQuery[],        // For queries
        labels: LContainer[],   // Labeling
        restricts: String[],    // Stuff to show on GUI
        hide: String[],         // Stuff to be ignored
    }

    constructor(props) {
        super(props)
        let hide = props.hide ? props.hide : []
        let sensitive = props.sensitive ? props.sensitive : []

        this.state = {
            queries: [],
            labels: [],
            restricts: props.restricts,
            hide: hide,
            // Options
            options: {
                r: 1
            },
            results: {},
            sensitive: sensitive
        }
    }

    changeName = (option: { name: String, query: LQuery, id: String }, type: String) => {
        const { queries, labels } = this.state
        const { name, query, id } = option

        let indexStr = id ? `${query.id}_${id}` : `${query.id}`
        let label = labels.find(l => l.type === type)

        query.name = name
        if (label) {
            let subQuery = {
                id: `${indexStr}_0`,
                name: ''
            }
            query.value = subQuery
            this.changeName(
                {
                    name: query?.value?.name,
                    query: query.value,
                    id: query.value.id
                }
            )
        } else {
            query.value = ''
        }

        this.setState({ queries: queries })
    }

    componentDidMount() {
        PublicService.labels()
        .then(response => {
            let data = response?.data?.filter(r => !this.state.hide.includes(r))
            let rs = (this.state.restricts && this.state.restricts.length !== 0)
                    ? this.state.restricts
                    : response.data.map(r => r.type)
                    
            rs = rs.filter(r => !this.state.hide.includes(r))

            this.setState(prev => ({
                labels: data,
                restricts: rs
            }))
        })
    }

    render() {
        const { queries, labels, restricts } = this.state;
        let activeR = restricts ? restricts[this.state.options.r] : 0
        let lcontainer = labels.find(l => l.type === activeR)
        const NF = <div className='text-center py-1'>
            Không có kết quả
        </div>
        return (
            <div className='container border rounded bg-light p-2 mt-3'>
                <div className='my-2'>
                    {restricts?.map((r, i) => (
                        <span
                            className={`m-1 p-2 r-option pointer rounded ${activeR === r ? 'active' : ''}`}
                            onClick={() => {
                                this.setState(prev => ({
                                    ...prev,
                                    queries: [],
                                    options: { r: i }
                                }))
                            }}
                        >{labels.find(l => l.type === r)?.label}</span>
                    ))}
                </div>
                
                <hr />
                <div className='my-2'>
                    {queries.map((q, index) => {
                        const valRecursively = function(valala) {
                            return valala.value?.id ? valRecursively(valala.value) : valala.value
                        }

                        return (
                            <>
                            <div className='mb-2 rounded px-2 py-1' style={{ backgroundColor: '#fbf3dd', border: '1px solid #dcc896' }}>
                                <Options 
                                    key={index} 
                                    lcontainer={lcontainer}
                                    query={q}
                                    labels={labels}
                                    sensitive={this.state.sensitive}
                                    onChange={(option, ltype) => this.changeName(option, ltype)}
                                />
                                <br />
                                <span 
                                    className='bg-danger border border-dark rounded px-2 py-1 me-1 pointer'
                                    onClick={() => {
                                        const { queries: qs } = this.state
                                        queries.splice(index, 1)

                                        this.setState({
                                            queries: qs
                                        })
                                    }}
                                >
                                    <FontAwesomeIcon className='text-white' icon={faTimes} />
                                </span>
                                <input 
                                    placeholder='Giá trị tìm kiếm...'
                                    className='w-50 border rounded py-1 px-2 mt-1 border-dark'
                                    type='text'
                                    value={valRecursively(q)}
                                    onChange={(e) => {
                                        const { queries: qqq } = this.state
                                        const setRecursively = (valala, value) => {
                                            if (valala.value?.id) {
                                                valala.value = setRecursively(valala.value, value)
                                                return valala
                                            } else {
                                                valala.value = value
                                                return valala
                                            }
                                        }
                                        q = setRecursively(q, e.target.value)
                                        qqq.splice(qqq.findIndex(qq => qq.id === q.id), 1, q)

                                        this.setState(prev => ({
                                            ...prev,
                                            queries: qqq
                                        }))
                                    }}
                                />
                            </div>
                            
                            <hr />
                            </>
                        )
                    })}

                    <br />
                    <button
                        className='btn btn-success py-0 px-1' 
                        onClick={() => {
                            queries.push({
                                id: `${queries.length}`,
                                name: lcontainer?.labels[0]?.name,
                                value: ''
                            })

                            this.setState({
                                query: queries
                            })
                        }}
                    >
                        <FontAwesomeIcon icon={faPlus} /> Thêm điều kiện
                    </button>

                    <button 
                        className='btn btn-outline-success ms-2 py-0 px-1' 
                        onClick={() => {
                            PublicService.searchAdvance(this.state.queries, activeR)
                            .then(response => {
                                if (response?.data) {
                                    this.setState({
                                        results: response.data
                                    })
                                }
                            })
                        }}
                    >
                        <FontAwesomeIcon icon={faSearch} /> Tìm kiếm
                    </button>
                </div>

                {<div 
                        className=' border border-dark rounded bg-white shadow'
                    >
                        {this.state.results?.results?.length > 0 ? 
                            (
                                <div className='px-2'>
                                    <b>{Map[this.state.results?.type].label}</b>
                                    <ul>
                                        {this.state.results.results?.map(({ url, value }) => (
                                            <li key={url}>
                                                <Link to={`${Map[this.state.results?.type].path}/${url}`}>
                                                    {value}
                                                </Link>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            ) :
                            <>{NF}</>
                        }
                    </div>} 
            </div>
        )
    }
}

class Options extends Component {    

    changeName = (e) => {
        this.selectedLType = e.target.selectedOptions[0].dataset.ltype
        this.props.onChange({ name: e.target.value, query: this.props.query }, this.selectedLType)
    }

    render() {
        const { lcontainer, query, labels, sensitive } = this.props

        return (
            <>
            <select
                // defaultValue={lcontainer?.labels[0]?.name}
                className='w-25 border rounded p-1'
                onChange={(e) => this.changeName(e)}
            >
                <option value='' hidden>------</option>
                {
                    lcontainer && <>
                        {lcontainer.labels
                        .filter(llabel => !sensitive?.includes(llabel.name))
                        .map((llabel, index) => (
                            <option 
                                key={llabel.name} 
                                value={llabel.name}
                                data-ltype={llabel.type}
                            >{lcontainer.label} | {llabel.value}</option>
                        ))}
                    </>
                }
            </select>
            {query?.value?.id && <><Options 
                    lcontainer={
                        labels
                        .map(l => {
                            let res = {...l}
                            res.labels = l.labels.filter(ll => ll.type !== this.selectedLType && ll.type !== lcontainer.type)
                            return res
                        })
                        .find(l => l.type === this.selectedLType)
                    } 
                    query={query.value}
                    labels={labels}
                    onChange={(option, ltype) => this.props.onChange(option, ltype)}
                    sensitive={sensitive}
                /></>}
            </>
        )
    }
}