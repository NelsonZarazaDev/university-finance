import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './financial-obligation.reducer';

export const FinancialObligationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const financialObligationEntity = useAppSelector(state => state.financialObligation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="financialObligationDetailsHeading">
          <Translate contentKey="universityFinanceApp.financialObligation.detail.title">FinancialObligation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{financialObligationEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="universityFinanceApp.financialObligation.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{financialObligationEntity.amount}</dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="universityFinanceApp.financialObligation.dueDate">Due Date</Translate>
            </span>
          </dt>
          <dd>
            {financialObligationEntity.dueDate ? (
              <TextFormat value={financialObligationEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="universityFinanceApp.financialObligation.status">Status</Translate>
            </span>
          </dt>
          <dd>{financialObligationEntity.status}</dd>
          <dt>
            <Translate contentKey="universityFinanceApp.financialObligation.student">Student</Translate>
          </dt>
          <dd>{financialObligationEntity.student ? financialObligationEntity.student.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/financial-obligation" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/financial-obligation/${financialObligationEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FinancialObligationDetail;
