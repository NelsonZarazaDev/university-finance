import React, { useEffect } from 'react';
import { Button, Col, FormText, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { ObligationStatus } from 'app/shared/model/enumerations/obligation-status.model';

import { createEntity, getEntity, reset, updateEntity } from './financial-obligation.reducer';

export const FinancialObligationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const students = useAppSelector(state => state.student.entities);
  const financialObligationEntity = useAppSelector(state => state.financialObligation.entity);
  const loading = useAppSelector(state => state.financialObligation.loading);
  const updating = useAppSelector(state => state.financialObligation.updating);
  const updateSuccess = useAppSelector(state => state.financialObligation.updateSuccess);
  const obligationStatusValues = Object.keys(ObligationStatus);

  const handleClose = () => {
    navigate(`/financial-obligation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudents({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }

    const entity = {
      ...financialObligationEntity,
      ...values,
      student: students.find(it => it.id.toString() === values.student?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'OUTSTANDING',
          ...financialObligationEntity,
          student: financialObligationEntity?.student?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="universityFinanceApp.financialObligation.home.createOrEditLabel" data-cy="FinancialObligationCreateUpdateHeading">
            <Translate contentKey="universityFinanceApp.financialObligation.home.createOrEditLabel">
              Create or edit a FinancialObligation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm mode="all" defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="financial-obligation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('universityFinanceApp.financialObligation.amount')}
                id="financial-obligation-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('universityFinanceApp.financialObligation.dueDate')}
                id="financial-obligation-dueDate"
                name="dueDate"
                data-cy="dueDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('universityFinanceApp.financialObligation.status')}
                id="financial-obligation-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {obligationStatusValues.map(obligationStatus => (
                  <option value={obligationStatus} key={obligationStatus}>
                    {translate(`universityFinanceApp.ObligationStatus.${obligationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="financial-obligation-student"
                name="student"
                data-cy="student"
                label={translate('universityFinanceApp.financialObligation.student')}
                type="select"
                required
              >
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/financial-obligation"
                replace
                variant="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FinancialObligationUpdate;
