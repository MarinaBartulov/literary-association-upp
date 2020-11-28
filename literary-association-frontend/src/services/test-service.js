import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TestService extends HttpService {
  test = async (payload) => {
    try {
      const response = await this.client.get(ROUTES.TEST);
      console.log(response);
      window.open(response.data.body)
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const testService = new TestService();
