import API from '../components/axios/API';

class PublicService {
    listCauHoi(id) {
        return API.get(`/public/cau-hoi/${id}`);
    }

    saveCauHoi(form) {
        return API.post('/public/cau-hoi', form);
    }

    findChuongById(id) {
        return API.get(`/public/chuong/${id}`);
    }

    findBaiGiangById(id) {
        return API.get(`/public/bai-giang/${id}`);
    }

    findTietById(id) {
        return API.get(`/public/tiet/${id}`);
    }

    listBaiGiang() {
        return API.get('public/bai-giang');
    }
}

export default new PublicService();