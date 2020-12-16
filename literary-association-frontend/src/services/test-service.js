import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TestService extends HttpService {
  test = async (payload) => {
    try {
      console.log(payload);
      const { data } = await this.client.post(ROUTES.TEST, payload);
      console.log(data);
      window.open(data.body.redirectionURL);
      //localStorage.setItem("merchantId", data.body.merchantId);
      return data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const testService = new TestService();
