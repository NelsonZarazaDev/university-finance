import financialObligation from 'app/entities/financial-obligation/financial-obligation.reducer';
import payment from 'app/entities/payment/payment.reducer';
import student from 'app/entities/student/student.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  student,
  financialObligation,
  payment,
  // jhipster-needle-add-reducer-combine - JHipster will add reducer here
};

export default entitiesReducers;
