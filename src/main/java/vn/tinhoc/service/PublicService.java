package vn.tinhoc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.TietRepository;

@Service
public class PublicService {
	
	@Autowired
	CauHoiRepository cauHoiRepository;
	
	@Autowired
	DapAnRepository dapAnRepository;

	@Autowired
	ChuongRepository chuongRepository;
	
	@Autowired
	BaiGiangRepository baiGiangRepository;
	
	@Autowired
	TietRepository tietRepository;
	
	@Autowired
	OntologyVariables vars;
	
	public CauHoi update(CauHoi cauHoi) {
		
		return null;
	}
	
	public CauHoiDTO create(CauHoiDTO cauHoiDTO) {
		CauHoi cauHoi = cauHoiDTO.getCauHoi();
		List<DapAn> dapAns = cauHoiDTO.getDapAns();
		
		cauHoi.setId(vars.getBaseUri() + "Cau2_KTC2_Lop6_1aaaa");
		cauHoiRepository.save(cauHoi);
		
		for (int i = 1; i <= dapAns.size(); i++) {
			DapAn dapAn = dapAns.get(i - 1);
			dapAn.setId("DapAn" + i);
			dapAn.setThuocCauHoi(cauHoi);
			dapAnRepository.save(dapAn);
		}
		
		return cauHoiDTO;
	}
	
	public List<CauHoiDTO> listCauHoi() {
		List<CauHoi> cauHois = cauHoiRepository.find();
		List<DapAn> dapAns = dapAnRepository.find();
		
		List<CauHoiDTO> cauHoiDTOS = new ArrayList<>();
		
		for(CauHoi cauHoi: cauHois ) {
			List<DapAn> DapAnTuongUng = 
					dapAns.stream()
						.filter(dapAn -> {
							if(dapAn.getThuocCauHoi() != null) {
								return dapAn.getThuocCauHoi().getId().equals(cauHoi.getId());
							}
							return false;
						})
						.collect(Collectors.toList());
			
			CauHoiDTO cauHoiDTO = new CauHoiDTO(cauHoi, DapAnTuongUng);
			cauHoiDTOS.add(cauHoiDTO);
		}
		return cauHoiDTOS;
	}
	
	public Chuong findChuongById(String id) {
		Optional<Chuong> op = chuongRepository.findByUriTag(id);
		Chuong chuong = op.orElse(null);
		List<Tiet> tietlist = tietRepository.find();
		
		//fill data vao list
		if(chuong != null){
			List<Tiet> tiets = chuong.getGomTiet();
					
			for (int i = 0; i < tiets.size(); i++) {
				for(int j = 0; j < tietlist.size(); j++) {
					if(tiets.get(i).getId().equals(tietlist.get(j).getId())) {
						tiets.set(i, tietlist.get(j));
						break;
					}
				}
			}
			chuong.setGomTiet(tiets);
		}
		return chuong;
	}
	
	public List<BaiGiang> listBaiGiang(){
		return baiGiangRepository.find();
	}
	
	public BaiGiang findBaiGiangById(String id) {
		Optional<BaiGiang> op = baiGiangRepository.findByUriTag(id);
		List<Chuong> chuonglist = chuongRepository.find();
		BaiGiang baigiang = op.orElse(null);
		
		//fill data vao list
		if(baigiang != null){
			List<Chuong> chuongs = baigiang.getGomChuong();
			
			for (int i = 0; i < chuongs.size(); i++) {
				for(int j = 0; j < chuonglist.size(); j++) {
					if(chuongs.get(i).getId().equals(chuonglist.get(j).getId())) {
						chuongs.set(i, chuonglist.get(j));
						break;
					}
				}
			}
			baigiang.setGomChuong(chuongs);
		}
		
		return baigiang;
	}
}