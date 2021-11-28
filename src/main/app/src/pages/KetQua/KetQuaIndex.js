import React, { Component } from 'react'
import PublicService from '../../services/PublicService'
import KiemTraBox from './KiemTraBox'

export default class KetQuaIndex extends Component {
    constructor(props) {
        super(props)

        this.state = {
            KQs: []
        }
    }

    componentDidMount() {
        PublicService.findKQs()
        .then(response => {
            if (response?.data) {
                this.setState({
                    KQs: response.data
                })
            }
        })
    }

    render() {
        return (
            <div className='container'>
                {this.state.KQs.map(KQ => {
                    return (
                        <KiemTraBox KetQua={KQ} />
                    )
                })}
            </div>
        )
    }
}
