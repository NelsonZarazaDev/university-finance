import dayjs from 'dayjs';

import { IFinancialObligation } from 'app/shared/model/financial-obligation.model';

export interface IPayment {
  id?: number;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
  financialObligation?: IFinancialObligation;
}

export const defaultValue: Readonly<IPayment> = {};
