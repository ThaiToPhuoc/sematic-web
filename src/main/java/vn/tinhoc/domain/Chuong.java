package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

import java.util.List;

@OntologyObject(uri = "Chuong")
public class Chuong {
    @Name
    private String id;
    
    private Integer SSTChuong;
    
    private String NoiDungChuong;

    private List<Tiet> GomTiet;
    
    private BaiGiang ThuocBaiGiang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSSTChuong() {
		return SSTChuong;
	}

	public void setSSTChuong(Integer sSTChuong) {
		SSTChuong = sSTChuong;
	}

	public String getNoiDungChuong() {
		return NoiDungChuong;
	}

	public void setNoiDungChuong(String noiDungChuong) {
		NoiDungChuong = noiDungChuong;
	}

	public List<Tiet> getGomTiet() {
        return GomTiet;
    }

    public void setGomTiet(List<Tiet> gomTiet) {
        GomTiet = gomTiet;
    }

	public BaiGiang getThuocBaiGiang() {
		return ThuocBaiGiang;
	}

	public void setThuocBaiGiang(BaiGiang thuocBaiGiang) {
		ThuocBaiGiang = thuocBaiGiang;
	}
    
}