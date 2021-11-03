import API from '../components/axios/API';

class CauHoiService {
    getListCauHoi() {
        return API.get(`cau-hoi/`);
    }
}