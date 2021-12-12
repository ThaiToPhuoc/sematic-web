import API from '../components/axios/API';

class AdminService {
    findGiang(id) {
        return API.get(`/admin/chuong/${id}`);
    }

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

    uploadVideo(form) {
        return API.post(`/admin/upload`, form, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        })
    }

    deleteVideo(id) {
        return API.delete(`/admin/delete-video`, {
            params: {
                id: id
            }
        })
    }

    deleteBaiGiang(form) {
        return API.put(`/admin/delete/bai-giang`, form)
    }

    deleteChuong(form) {
        return API.put(`/admin/delete/chuong`, form)
    }

    deleteKiemTra(form) {
        return API.put(`/admin/delete/kiem-tra`, form)
    }
}

export default new AdminService();