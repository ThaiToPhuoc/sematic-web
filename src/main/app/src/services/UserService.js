import API from '../components/axios/API';

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