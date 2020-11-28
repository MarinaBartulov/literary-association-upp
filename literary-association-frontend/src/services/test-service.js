import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TestService extends HttpService {
  test = async (payload) => {
    try {
      const response = await this.client.post(ROUTES.TEST, payload);
      console.log(response);
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const testService = new TestService();
