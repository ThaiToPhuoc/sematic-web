import API from '../components/axios/API';

class PublicService {
    listCauHoi() {
        return API.get('/public/cau-hoi');
    }

    saveCauHoi(form) {
        return API.post('/public/cau-hoi', form);
    }
}

export default new PublicService();