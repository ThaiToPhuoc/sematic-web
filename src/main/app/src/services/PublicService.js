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

    nopBai(form, ktra) {
        return API.post('/public/nop-bai', form, {
            params: {
                user: JSON.parse(sessionStorage.getItem('user')).username,
                ktra: ktra
            }
        });
    }

    findKQs() {
        return API.get(`/public/ket-qua/${JSON.parse(sessionStorage.getItem('user')).username}`);
    }

    searchBasic(form) {
        return API.post(`public/search/basic`, form)
    }

    searchAdvance(form, type) {
        return API.post(`public/search/advance/${type}`, form)
    }

    labels() {
        return API.get(`public/search/labels`)
    }
}

export default new PublicService();