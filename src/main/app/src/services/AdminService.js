import API from '../components/axios/API';

class AdminService {
    findAllBaiGiang() {
        return API.get(`/admin/bai-giang`);
    }

    createBaiGiang(form) {
        return API.post(`/admin/bai-giang`, form);
    }
}

export default new AdminService();