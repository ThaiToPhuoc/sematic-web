import API from '../components/axios/API';

class AdminService {
    findAllBaiGiang() {
        return API.get(`/admin/bai-giang`);
    }

    createBaiGiang(form) {
        return API.post(`/admin/bai-giang`, form);
    }

    updateChuong(form) {
        return API.put(`/admin/chuong`, form);
    }

    createChuong(form) {
        return API.post(`/admin/chuong`, form);
    }
}

export default new AdminService();