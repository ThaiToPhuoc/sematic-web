import axios from "axios";
import Notify from "../notify/Notify";
import AuthHeader from "./AuthHeader";
const SERVER_URL = process.env.REACT_APP_SERVER_URL;

const Server = axios.create({
    baseURL: SERVER_URL,
    headers: {
        "Content-type": "application/json",
        "Authorization": AuthHeader()
    }
});

Server.interceptors.response.use(undefined, (error) => {
    const message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
    Notify.error(message);

    return Promise.reject(error);
})

export default Server;