import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class BookService extends HttpService {
  startPlagiarsmDetection = async () => {
    try {
      const response = await this.client.get(
        ROUTES.START_PLAGIARSM_DETECTION
      );
      console.log(response.data);
      return response.data;
    } catch (e) {
      console.log(e);
    }
  };
}

export const bookService = new BookService();