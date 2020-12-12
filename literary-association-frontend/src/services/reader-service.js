import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class ReaderService extends HttpService {
  regFormFields = async (payload) => {
    try {
      const response = await this.client.get(ROUTES.READER_REG_FORM);
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };

  regReader = async (payload, taskId) => {
    try {
      const response = await this.client.post(
        ROUTES.READER_REGISTER + "/" + taskId,
        payload
      );
      console.log(response);
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  activateAccount = async (processId, token) => {
    try {
      const response = await this.client.put(
        ROUTES.READER_ACTIVATION + "/" + processId + "/" + token
      );
      console.log(response.data);
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const readerService = new ReaderService();
