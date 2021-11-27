package vn.tinhoc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.domain.dto.BaiGiangDTO;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.domain.dto.KetQuaDTO;
import vn.tinhoc.domain.dto.NopBaiDTO;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KiemTraRepository;
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
	KiemTraRepository kiemtraRepository;
	
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
	
	public List<CauHoiDTO> listCauHoi(String id) {
		Optional<KiemTra> op = kiemtraRepository.findByUriTag(id);
		KiemTra kiemtra = op.orElse(null);
		List<CauHoi> cauhoilist = cauHoiRepository.find();
		List<CauHoi> cauHois = kiemtra.getGomCauHoi();
		for (int i = 0; i < cauHois.size(); i++) {
			for(int j = 0; j < cauhoilist.size(); j++) {
				if(cauHois.get(i).getId().equals(cauhoilist.get(j).getId())) {
					cauHois.set(i, cauhoilist.get(j));
					break;
				}
			}
		}
		
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
	
	public BaiGiangDTO findBaiGiangById(String id) {
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
		
		List<KiemTra> kiemTras = kiemtraRepository.find();
		
		List<KiemTra> KiemTraTuongUng = 
				kiemTras.stream()
					.filter(kiemTra -> {
						if(kiemTra.getThuocBaiGiang() != null) {
							return kiemTra.getThuocBaiGiang().getId().equals(baigiang.getId());
						}
						return false;
					})
					.collect(Collectors.toList());

		return new BaiGiangDTO(baigiang, KiemTraTuongUng);
	}
	
	public Tiet findTietById(String id) {
		Optional<Tiet> op = tietRepository.findByUriTag(id);
		return op.orElse(null);
	}

	public List<KetQuaDTO> nopBai(List<NopBaiDTO> nopBaiDTOs) {
		List<KetQuaDTO> ketQuaDTOS = new ArrayList<>();
		nopBaiDTOs.forEach(nopBaiDTO -> {
			Optional<CauHoi> cauHoiOP = cauHoiRepository.findByUriTag(nopBaiDTO.getCauHoi());

			if (cauHoiOP.isPresent()) {
				CauHoi cauHoi = cauHoiOP.get();

				String tuKhoaSTR = cauHoi.getTuKhoa();
				Supplier<Stream<String>> tuKhoasStream = () -> Arrays
						.stream(StringUtils.split(tuKhoaSTR, ";"));

				Set<String> tuKhoaSet = tuKhoasStream.get().collect(Collectors.toSet());
				String tuKhoas = tuKhoasStream
						.get()
						.map(s -> String.format("regex(?o, \"%s\", \"i\")", s.toLowerCase(Locale.ROOT)))
						.collect(Collectors.joining(" || "));

				String select = String.format(
					"SELECT ?s " +
					"WHERE {" +
					"	?s rdf:type thoc:Tiet ." +
					"	?s thoc:TuKhoa ?o ." +
					"	FILTER (%s)" +
					"}",
					tuKhoas
				);

				System.out.println(select);

				List<Tiet> tiets = tietRepository.query(select);
				KetQuaDTO ketQuaDTO = new KetQuaDTO(nopBaiDTO);
				ketQuaDTO.tuKhoas.addAll(tuKhoaSet);
				ketQuaDTO.chuongs.addAll(KetQuaDTO.GroupBy.group(tiets));
				ketQuaDTOS.add(ketQuaDTO);
			}
		});

		return ketQuaDTOS;
	}
}