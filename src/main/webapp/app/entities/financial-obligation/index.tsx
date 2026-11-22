import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FinancialObligation from './financial-obligation';
import FinancialObligationDeleteDialog from './financial-obligation-delete-dialog';
import FinancialObligationDetail from './financial-obligation-detail';
import FinancialObligationUpdate from './financial-obligation-update';

const FinancialObligationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FinancialObligation />} />
    <Route path="new" element={<FinancialObligationUpdate />} />
    <Route path=":id">
      <Route index element={<FinancialObligationDetail />} />
      <Route path="edit" element={<FinancialObligationUpdate />} />
      <Route path="delete" element={<FinancialObligationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FinancialObligationRoutes;
