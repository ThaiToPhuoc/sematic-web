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

    findKiemTraByBaiGiang(id) {
        return API.get(`/admin/bai-giang/${id}/kiem-tra`)
    }

    updateKiemTra(form) {
        return API.put(`/admin/kiem-tra`, form);
    }

    createKiemTra(form) {
        return API.post(`/admin/kiem-tra`, form);
    }
}

export default new AdminService();