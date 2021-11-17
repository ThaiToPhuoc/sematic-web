package vn.tinhoc.domain.dto;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.KiemTra;

@OntologyObject(uri = "BaiGiang")
public class BaiGiangDTO {
	 @Name
	 private BaiGiang baiGiang;
	 
	 private List<KiemTra> kiemTras;

	public BaiGiangDTO(BaiGiang baiGiang, List<KiemTra> kiemTras) {
		super();
		this.baiGiang = baiGiang;
		this.kiemTras = kiemTras;
	}

	public BaiGiang getBaiGiang() {
		return baiGiang;
	}

	public void setBaiGiang(BaiGiang baiGiang) {
		this.baiGiang = baiGiang;
	}

	public List<KiemTra> getKiemTras() {
		return kiemTras;
	}

	public void setKiemTras(List<KiemTra> kiemTras) {
		this.kiemTras = kiemTras;
	}
	 
	 
}
