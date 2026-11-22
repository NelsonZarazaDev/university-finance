import dayjs from 'dayjs';

import { StudentStatus } from 'app/shared/model/enumerations/student-status.model';

export interface IStudent {
  id?: number;
  studentCode?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  enrollmentDate?: dayjs.Dayjs;
  status?: keyof typeof StudentStatus;
}

export const defaultValue: Readonly<IStudent> = {};
