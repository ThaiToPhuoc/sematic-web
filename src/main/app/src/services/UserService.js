import API from '../components/axios/API';
import AuthHeader from '../components/axios/AuthHeader';
import Notify from '../components/notify/Notify';

class UserService {
    login(form) {
        return API.post(`/user/login`, form)
        .then(response => {
            if (response?.data?.token) {
                sessionStorage.setItem('user', JSON.stringify(response.data));
            }

            return response?.data;
        })
    }

    register(form) {
        return API.post(`/user/register`, form);
    }

    logout() {
        sessionStorage.clear();
    }
}

export default new UserService();