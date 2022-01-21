import React, { Component } from 'react'
import { TruncateSharp } from '../../components/helpers/FieldValidate';
import KetQuaBox from './KetQuaBox'

interface Mapped {
    CHoi?: Object,
    DAnChon?: Object,
    DAnDung?: Object,
    GoiY?: Object
}

export default class KiemTraBox extends Component {

    mapKQ = (KetQua): Mapped[] => {
        const { kqKiemTra, ketQuaDTOS, cauHoiDTOS } = KetQua;

        return kqKiemTra.cuaBaiKiemTra?.gomCauHoi.map(CHoi => {
            let mapped: Mapped = {
                CHoi: CHoi,
                DAnChon: kqKiemTra.gomDapAn?.find(DA => DA?.thuocCauHoi?.id === CHoi.id),
                DAnDung: cauHoiDTOS.find(dto => dto?.cauHoi?.id === CHoi.id)
                                    .dapAns.find(DA => DA.ketQua),
                GoiY: ketQuaDTOS.find(dto => dto.cauHoi === CHoi.id)
            }

            return mapped
        })
    }

    getScore = (KQ) => {
        let score = 0
        KQ.cauHoiDTOS.forEach(dto => {
            let DAnDung = dto.dapAns.find(da => da.ketQua)

            let TLDung = KQ?.kqKiemTra?.gomDapAn?.find(da => da.id === DAnDung?.id)
            if (TLDung) score++
        })

        return `${score}/${KQ.cauHoiDTOS.length} câu đúng`
    }

    render() {
        const { KetQua } = this.props

        return (
            <div className='border rounded mt-2 mx-auto p-2'>
                <h6>Kiểm tra: {TruncateSharp(KetQua.kqKiemTra.cuaBaiKiemTra.id)}</h6>
                <h6>Kết quả: <b>{this.getScore(KetQua)}</b></h6>
                <hr />
                <div class="row gx-2">
                    {this.mapKQ(KetQua).map(mapped => (
                        <KetQuaBox {...mapped} />
                    ))}
                </div>
            </div>
        )
    }
}
