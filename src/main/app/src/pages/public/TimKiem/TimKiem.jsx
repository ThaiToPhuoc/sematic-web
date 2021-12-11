import React, { Component } from 'react'
import AdvanceSearch from '../../../components/Search/AdvanceSearch'
import PublicService from '../../../services/PublicService'

export default class TimKiem extends Component {
    constructor(props) {
        super(props)

        this.state = {
            labels: []
        }
    }

    render() {
        return (
            <div>
                <AdvanceSearch 
                    labels={this.state.labels}
                    hide={['CauHoi']}
                    restricts={['BaiGiang', 'KiemTra', 'Chuong', 'Tiet']}
                />
            </div>
        )
    }
}
