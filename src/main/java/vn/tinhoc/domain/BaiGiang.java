package vn.tinhoc.domain;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "BaiGiang")
public class BaiGiang {
	@Name
	private String id;
	
	private Integer ChuongTrinh ;
	
	private Integer HocKy ;
	
	private Integer NamHoc ;
	
	private GiaoVien DuocSoanBoi;
	
	private List<Chuong> GomChuong;

	public GiaoVien getDuocSoanBoi() {
		return DuocSoanBoi;
	}

	public List<Chuong> getGomChuong() {
		return GomChuong;
	}

	public void setGomChuong(List<Chuong> gomChuong) {
		GomChuong = gomChuong;
	}

	public void setDuocSoanBoi(GiaoVien duocSoanBoi) {
		DuocSoanBoi = duocSoanBoi;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getChuongTrinh() {
		return ChuongTrinh;
	}

	public void setChuongTrinh(Integer chuongTrinh) {
		ChuongTrinh = chuongTrinh;
	}

	public Integer getHocKy() {
		return HocKy;
	}

	public void setHocKy(Integer hocKy) {
		HocKy = hocKy;
	}

	public Integer getNamHoc() {
		return NamHoc;
	}

	public void setNamHoc(Integer namHoc) {
		NamHoc = namHoc;
	}

}
