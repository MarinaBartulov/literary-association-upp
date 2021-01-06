import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TestService extends HttpService {
  pay = async (payload) => {
    try {
      const { data } = await this.client.post(ROUTES.PAY, payload);
      console.log(data);
      window.open(data.body.redirectionURL);
      return data;
    } catch (e) {
      console.log(e);
    }
  };

  subscribe = async (payload) => {
    try {
      const { data } = await this.client.post(ROUTES.SUBSCRIBE, payload);
      console.log(data);
      window.open(data.body);
      return data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const testService = new TestService();
