import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class WriterService extends HttpService {
  startReg = async () => {
    const response = await this.client.get(ROUTES.WRITER_START_REG);
    console.log(response.data);
    return response.data;
  };

  activateAccount = async (processId, token) => {
    const response = await this.client.put(
      ROUTES.WRITER_ACTIVATION + "/" + processId + "/" + token
    );
    console.log(response.data);
    return response.data;
  };
}

export const writerService = new WriterService();
