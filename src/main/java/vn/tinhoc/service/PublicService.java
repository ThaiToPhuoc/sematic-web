package vn.tinhoc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;
import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.KQKiemTra;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.domain.dto.BaiGiangDTO;
import vn.tinhoc.domain.dto.BasicDTO;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.domain.dto.KQKtraDTO;
import vn.tinhoc.domain.dto.KetQuaDTO;
import vn.tinhoc.domain.dto.NopBaiDTO;
import vn.tinhoc.domain.dto.User;
import vn.tinhoc.repository.BaiGiangRepository;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.ChuongRepository;
import vn.tinhoc.repository.DapAnRepository;
import vn.tinhoc.repository.KQKiemTraRepository;
import vn.tinhoc.repository.KiemTraRepository;
import vn.tinhoc.repository.TietRepository;
import vn.tinhoc.repository.UserRepository;
import vn.tinhoc.tokenizer.Dictionary;
import vn.tinhoc.tokenizer.TokenObject;
import vn.tinhoc.utils.DataUtils;
import vn.tinhoc.utils.search.GenericSearch;

import static vn.tinhoc.utils.DataUtils.removeSharp;

@Service
public class PublicService {

	@Autowired Dictionary dictionary;
	@Autowired OntologyVariables vars;
	@Autowired TokenObject tokenObject;
	@Autowired TietRepository tietRepository;
	@Autowired UserRepository userRepository;
	@Autowired DapAnRepository dapAnRepository;
	@Autowired CauHoiRepository cauHoiRepository;
	@Autowired ChuongRepository chuongRepository;
	@Autowired KiemTraRepository kiemtraRepository;
	@Autowired BaiGiangRepository baiGiangRepository;
	@Autowired KQKiemTraRepository kqKiemTraRepository;
	
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

	public List<KetQuaDTO> nopBai(List<NopBaiDTO> nopBaiDTOs, String user, String ktra) {
		List<KetQuaDTO> ketQuaDTOS = new ArrayList<>();
		nopBaiDTOs.forEach(nopBaiDTO -> {
			Optional<CauHoi> cauHoiOP = cauHoiRepository.findByUriTag(nopBaiDTO.getCauHoi());

			cauHoiOP.ifPresent(cauHoi -> searchTuKhoa(ketQuaDTOS, cauHoi));
		});

		saveKetQua(nopBaiDTOs, user, ktra);
		return ketQuaDTOS;
	}

	private void saveKetQua(List<NopBaiDTO> nopBaiDTOs, String user, String ktra) {
		// KQKiemTra id = KQKTra_<User id>_<n>
		User userInOWL = userRepository.findByPropertyValue("TenTaiKhoan", user).orElseThrow();
		KiemTra kiemTra = kiemtraRepository.findByUriTag(ktra).orElseThrow();
		KQKiemTra kqKiemTra = new KQKiemTra();
		int newIndex = 1;
		List<KQKiemTra> kqKiemTras = kqKiemTraRepository.query(
			"SELECT ?s " +
					"WHERE { " +
					"	?s rdf:type thoc:KQKiemTra ." +
					"	?s thoc:CuaHocSinh thoc:" + user + ". " +
					" }"
		);

		if (!CollectionUtils.isEmpty(kqKiemTras)) {
			newIndex = DataUtils.smallestMissingNumber(
					kqKiemTras.stream()
							.map(kq -> {
								int lastIndexSpace = kq.getId().lastIndexOf('_');
								return Integer.parseInt(kq.getId().substring(lastIndexSpace + 1));
							}).toArray(Integer[]::new)
			);
		}

		String KQId = "KQKTra_" + user + "_" + newIndex;

		kqKiemTra.setId(KQId);
		kqKiemTra.setCuaHocSinh(userInOWL);
		kqKiemTra.setCuaBaiKiemTra(kiemTra);
		kqKiemTra.setGomDapAn(
			nopBaiDTOs.stream()
					.map(nopBaiDTO -> {
						if (StringUtils.isNotBlank(nopBaiDTO.getDapAn())) {
							return dapAnRepository
									.findByUriTag(nopBaiDTO.getDapAn())
									.orElse(new DapAn());
						}

						return new DapAn();
					})
					.filter(dapAn -> StringUtils.isNotBlank(dapAn.getId()))
					.collect(Collectors.toList())
		);

		kqKiemTraRepository.save(kqKiemTra);
	}

	public List<KQKtraDTO> findKQByUsername(String username) {
		List<KQKiemTra> kqKiemTras = kqKiemTraRepository.query(
				"SELECT ?s " +
						"WHERE { " +
						"	?s rdf:type thoc:KQKiemTra ." +
						"	?s thoc:CuaHocSinh thoc:" + username + ". " +
						" }"
		);
		List<KQKtraDTO> kqKtraDTOS = new ArrayList<>();
		kqKiemTras.forEach(kqKiemTra -> {
			List<KetQuaDTO> ketQuaDTOS = new ArrayList<>();
			kqKiemTra.getCuaBaiKiemTra()
				.getGomCauHoi()
				.forEach(cauHoi -> {
					searchTuKhoa(ketQuaDTOS, cauHoi);
				});

			KQKtraDTO kqKtraDTO = new KQKtraDTO();
			kqKtraDTO.setKqKiemTra(kqKiemTra);
			kqKtraDTO.setKetQuaDTOS(ketQuaDTOS);
			kqKtraDTO.setCauHoiDTOS(listCauHoi(kqKiemTra.getCuaBaiKiemTra().getId()));
			kqKtraDTOS.add(kqKtraDTO);
		});

		return kqKtraDTOS;
	}

	private void searchTuKhoa(List<KetQuaDTO> ketQuaDTOS, CauHoi cauHoi) {
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

		List<Tiet> tiets = tietRepository.query(select);
		KetQuaDTO ketQuaDTO = new KetQuaDTO(cauHoi.getId());
		ketQuaDTO.tuKhoas.addAll(tuKhoaSet);
		ketQuaDTO.chuongs.addAll(KetQuaDTO.GroupBy.group(tiets));
		ketQuaDTOS.add(ketQuaDTO);
	}

	public Object basicSearch(String text) {
		List<Map<String, Object>> results = new ArrayList<>();
		GenericSearch genericSearch = new GenericSearch(vars, tokenObject, dictionary);

		Pair<String, List<BasicDTO>> tiStr = genericSearch.parse(text, Tiet.class);
		Pair<String, List<BasicDTO>> chStr = genericSearch.parse(text, Chuong.class);
		Pair<String, List<BasicDTO>> ktStr = genericSearch.parse(text, KiemTra.class);
		Pair<String, List<BasicDTO>> bgStr = genericSearch.parse(text, BaiGiang.class);

		List<Tiet> tiets = tietRepository.query(tiStr.getKey());
		List<Chuong> chuongs = chuongRepository.query(chStr.getKey());
		List<KiemTra> kiemTras = kiemtraRepository.query(ktStr.getKey());
		List<BaiGiang> baiGiangs = baiGiangRepository.query(bgStr.getKey());

		// BaiGiang -> [ Chuong -> [ Tiet ] ] -> { }
		// KiemTra -> { }

		results.add(Map.ofEntries(
			Map.entry("type", "baigiang"),
			Map.entry("results",
				baiGiangs.stream().map(baiGiang ->
					Map.ofEntries(
						Map.entry("url", removeSharp(baiGiang.getId())),
						Map.entry("value", String.format(
							"C-Trình lớp %s - H-Kỳ %d",
								baiGiang.getChuongTrinh(),
								baiGiang.getHocKy()
						)
					)
				))
			)
		));

		results.add(Map.ofEntries(
			Map.entry("type", "chuong"),
			Map.entry("results",
				chuongs.stream().map(chuong ->
					Map.ofEntries(
						Map.entry("url", removeSharp(chuong.getId())),
						Map.entry("value", String.format(
								"Chương %d - %s",
								chuong.getSTTChuong(),
								chuong.getNoiDungChuong()
							)
						)
					))
			)
		));

		results.add(Map.ofEntries(
			Map.entry("type", "tiet"),
			Map.entry("results",
				tiets.stream().map(tiet ->
					Map.ofEntries(
						Map.entry("url", removeSharp(tiet.getId())),
						Map.entry("value", String.format(
								"Chương %d Tiết %d - %s",
								tiet.getThuocChuong().getSTTChuong(),
								tiet.getSTTTiet(),
								tiet.getLink()
							)
						)
					))
			)
		));

		results.add(Map.ofEntries(
			Map.entry("type", "kiemtra"),
			Map.entry("results",
				kiemTras.stream().map(kiemTra -> {
					BaiGiang bg = kiemTra.getThuocBaiGiang();
					String id = removeSharp(kiemTra.getId());
					return Map.ofEntries(
							Map.entry("url", removeSharp(id)),
							Map.entry("value", String.format(
								"Kiểm tra %s: C-Trình lớp %s - H-Kỳ %d",
								id, bg.getChuongTrinh(), bg.getHocKy()
							)
						));
					}
				)
			)
		));

		return results;
	}

	public Object advanceSearch(String query, String type) {
		switch (type) {
			case "BaiGiang":
				return Map.ofEntries(
					Map.entry("type", "baigiang"),
					Map.entry("results",
						baiGiangRepository
							.query(query)
							.stream().map(baiGiang ->
								Map.ofEntries(
									Map.entry("url", removeSharp(baiGiang.getId())),
									Map.entry("value", String.format(
											"C-Trình lớp %s - H-Kỳ %d",
											baiGiang.getChuongTrinh(),
											baiGiang.getHocKy()
										)
									)
								)
							)
					)
				);
			case "KiemTra":
				return Map.ofEntries(
						Map.entry("type", "kiemtra"),
						Map.entry("results",
							kiemtraRepository
								.query(query)
								.stream()
								.map(kiemTra -> {
									BaiGiang bg = kiemTra.getThuocBaiGiang();
									String id = removeSharp(kiemTra.getId());
									return Map.ofEntries(
										Map.entry("url", removeSharp(id)),
										Map.entry("value", String.format(
												"Kiểm tra %s: C-Trình lớp %s - H-Kỳ %d",
												id, bg.getChuongTrinh(), bg.getHocKy()
											)
										)
									);
								}
							)
						)
				);
			case "Chuong":
				return Map.ofEntries(
						Map.entry("type", "chuong"),
						Map.entry("results",
							chuongRepository
								.query(query)
								.stream()
								.map(chuong ->
								Map.ofEntries(
									Map.entry("url", removeSharp(chuong.getId())),
									Map.entry("value", String.format(
											"Chương %d - %s",
											chuong.getSTTChuong(),
											chuong.getNoiDungChuong()
										)
									)
								)
							)
						)
				);
			case "Tiet":
				return Map.ofEntries(
					Map.entry("type", "tiet"),
					Map.entry("results",
						tietRepository
							.query(query)
							.stream()
							.map(tiet ->
							Map.ofEntries(
								Map.entry("url", removeSharp(tiet.getId())),
								Map.entry("value", String.format(
										"Chương %d Tiết %d - %s",
										tiet.getThuocChuong().getSTTChuong(),
										tiet.getSTTTiet(),
										tiet.getLink()
									)
								)
							)
						)
					)
				);

			default:
				return Collections.emptyList();
		}
	}
}