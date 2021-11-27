package vn.tinhoc.domain.dto;

import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.Tiet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class KetQuaDTO {
    public final String cauHoi;
    public final Set<String> tuKhoas;
    public final List<GroupBy> chuongs;

    public KetQuaDTO(NopBaiDTO nopBaiDTO) {
        this(nopBaiDTO.getCauHoi());
    }

    public KetQuaDTO(String cauHoi) {
        this.cauHoi = cauHoi;
        tuKhoas = new HashSet<>();
        chuongs = new ArrayList<>();
    }

    public static class GroupBy {
        public final String chuongId;
        public final String noiDungChuong;
        public final List<Tiet> tiets;

        public static List<GroupBy> group(List<Tiet> tiets) {
            List<GroupBy> groups = new ArrayList<>();

            tiets.forEach(tiet -> {
                Chuong chuong = tiet.getThuocChuong();
                GroupBy group = groups.stream()
                        .filter(g -> g.chuongId.equalsIgnoreCase(chuong.getId()))
                        .findFirst()
                        .orElse(new GroupBy(
                            chuong.getId(),
                            chuong.getNoiDungChuong(),
                            new ArrayList<>()
                        ));

                group.tiets.add(tiet);
                groups.add(group);
            });

            return groups;
        }

        public GroupBy(String chuongId, String noiDungChuong, List<Tiet> tiets) {
            this.chuongId = chuongId;
            this.noiDungChuong = noiDungChuong;
            this.tiets = tiets;
        }
    }
}
