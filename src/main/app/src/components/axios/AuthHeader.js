export default function AuthHeader() {
    try {
        const user = JSON.parse(sessionStorage.getItem('user'));
  
        if (user.token) {
            return 'Bearer ' + user.token;
        } else {
            return '';
        }
    } catch { return '' }
}