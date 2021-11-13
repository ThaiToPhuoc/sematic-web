import API from '../components/axios/API';

class PublicService {
    listCauHoi() {
        return API.get('/public/cau-hoi');
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

    listBaiGiang() {
        return API.get('public/bai-giang');
    }
}

export default new PublicService();