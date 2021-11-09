package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "Tiet")
public class Tiet {
    @Name
    private String id;

    private String NoiDungTiet;

    private String TuKhoa;

    private Chuong ThuocChuong;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiDungTiet() {
        return NoiDungTiet;
    }

    public void setNoiDungTiet(String noiDungTiet) {
        NoiDungTiet = noiDungTiet;
    }

    public String getTuKhoa() {
        return TuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        TuKhoa = tuKhoa;
    }

    public Chuong getThuocChuong() {
        return ThuocChuong;
    }

    public void setThuocChuong(Chuong thuocChuong) {
        ThuocChuong = thuocChuong;
    }
}
