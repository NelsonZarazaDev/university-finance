import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">
          <Translate contentKey="universityFinanceApp.student.detail.title">Student</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="studentCode">
              <Translate contentKey="universityFinanceApp.student.studentCode">Student Code</Translate>
            </span>
          </dt>
          <dd>{studentEntity.studentCode}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="universityFinanceApp.student.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="universityFinanceApp.student.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="universityFinanceApp.student.email">Email</Translate>
            </span>
          </dt>
          <dd>{studentEntity.email}</dd>
          <dt>
            <span id="enrollmentDate">
              <Translate contentKey="universityFinanceApp.student.enrollmentDate">Enrollment Date</Translate>
            </span>
          </dt>
          <dd>
            {studentEntity.enrollmentDate ? (
              <TextFormat value={studentEntity.enrollmentDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="universityFinanceApp.student.status">Status</Translate>
            </span>
          </dt>
          <dd>{studentEntity.status}</dd>
        </dl>
        <Button as={Link as any} to="/student" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/student/${studentEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
