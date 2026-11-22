import dayjs from 'dayjs';

import { ObligationStatus } from 'app/shared/model/enumerations/obligation-status.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IFinancialObligation {
  id?: number;
  amount?: number;
  dueDate?: dayjs.Dayjs;
  status?: keyof typeof ObligationStatus;
  student?: IStudent;
}

export const defaultValue: Readonly<IFinancialObligation> = {};
