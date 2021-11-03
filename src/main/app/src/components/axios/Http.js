import axios from "axios";
import Notify from "../notify/Notify";

axios.interceptors.response.use(undefined, (error) => {
    const message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
    Notify.error(message);

    return Promise.reject(error);
})