import axios from 'axios';

export interface ICreatePaymentRequest {
  financialObligationId: number;
  amount: number;
}

export const createPaymentOperation = (paymentRequest: ICreatePaymentRequest) => axios.post('/api/payment-operations', paymentRequest);

export default {
  createPaymentOperation,
};
