import axios from 'axios';
import { IStudentOption } from 'app/shared/model/student-option.model';

const apiUrl = '/api/student-options';

export const getStudentOptions = async (): Promise<IStudentOption[]> => {
  const response = await axios.get<IStudentOption[]>(apiUrl);
  return response.data;
};
