import { BehaviorSubject } from 'rxjs';
import { toast, Slide } from 'react-toastify';

const notifications = new BehaviorSubject(null);

class NotificationService {
  notifications = notifications.asObservable();
  configuration = {
    position: toast.POSITION.TOP_RIGHT,
    transition: Slide
  }

  success = (msg) => {
    this.sendNotification(msg, AlertTypes.success)
  }

  info = (msg) => {
    this.sendNotification(msg, AlertTypes.info)
  }

  warn = (msg) => {
    this.sendNotification(msg, AlertTypes.warn)
  }

  error = (msg) => {
    this.sendNotification(msg, AlertTypes.error)
  }

  default = (msg) => {
    this.sendNotification(msg)
  }

  sendNotification = (msg, type) => {
    try {
      if (msg) {

        switch (type) {
          case AlertTypes.success:
            notifications.next(() => toast.success(msg, this.configuration));
            break;
          case AlertTypes.info:
            notifications.next(() => toast.info(msg, this.configuration));
            break;
          case AlertTypes.warn:
            notifications.next(() => toast.warn(msg, this.configuration));
            break;
          case AlertTypes.error:
            notifications.next(() => toast.error(msg, this.configuration));
            break;
          default:
            notifications.next(() => toast(msg, this.configuration));
            break;
        }
      }
    } catch (ex) {
      notifications.next(() => toast.error(ex.message, this.configuration));
    }
    notifications.next(null);
  }
}

const Notify = new NotificationService();

export default Notify;

export const AlertTypes = Object.freeze({
  success: Symbol('success'),
  info: Symbol('info'),
  warn: Symbol('warn'),
  error: Symbol('error')
});