import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Student from './student';
import StudentDeleteDialog from './student-delete-dialog';
import StudentDetail from './student-detail';
import StudentUpdate from './student-update';

const StudentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Student />} />
    <Route path="new" element={<StudentUpdate />} />
    <Route path=":id">
      <Route index element={<StudentDetail />} />
      <Route path="edit" element={<StudentUpdate />} />
      <Route path="delete" element={<StudentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentRoutes;
