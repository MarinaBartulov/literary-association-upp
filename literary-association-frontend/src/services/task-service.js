import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class TaskService extends HttpService {
  getFormFields = async (taskId) => {
    const response = await this.client.get(
      ROUTES.TASK_FORM_FIELDS + "/" + taskId
    );
    console.log(response.data);
    return response.data;
  };

  commitForm = async (payload, taskId) => {
    const response = await this.client.post(
      ROUTES.TASK_COMMIT_FORM + "/" + taskId,
      payload
    );
    console.log(response.data);
    return response.data;
  };
}

export const taskService = new TaskService();
