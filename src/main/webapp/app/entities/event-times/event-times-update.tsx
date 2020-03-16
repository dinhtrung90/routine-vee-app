import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IHabit } from 'app/shared/model/habit.model';
import { getEntities as getHabits } from 'app/entities/habit/habit.reducer';
import { getEntity, updateEntity, createEntity, reset } from './event-times.reducer';
import { IEventTimes } from 'app/shared/model/event-times.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEventTimesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EventTimesUpdate = (props: IEventTimesUpdateProps) => {
  const [habitId, setHabitId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { eventTimesEntity, habits, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/event-times' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getHabits();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    if (errors.length === 0) {
      const entity = {
        ...eventTimesEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="routineveeApp.eventTimes.home.createOrEditLabel">
            <Translate contentKey="routineveeApp.eventTimes.home.createOrEditLabel">Create or edit a EventTimes</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : eventTimesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="event-times-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="event-times-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dayOfWeekLabel" for="event-times-dayOfWeek">
                  <Translate contentKey="routineveeApp.eventTimes.dayOfWeek">Day Of Week</Translate>
                </Label>
                <AvField
                  id="event-times-dayOfWeek"
                  type="string"
                  className="form-control"
                  name="dayOfWeek"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startTimeLabel" for="event-times-startTime">
                  <Translate contentKey="routineveeApp.eventTimes.startTime">Start Time</Translate>
                </Label>
                <AvInput
                  id="event-times-startTime"
                  type="datetime-local"
                  className="form-control"
                  name="startTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.eventTimesEntity.startTime)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endTimeLabel" for="event-times-endTime">
                  <Translate contentKey="routineveeApp.eventTimes.endTime">End Time</Translate>
                </Label>
                <AvInput
                  id="event-times-endTime"
                  type="datetime-local"
                  className="form-control"
                  name="endTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.eventTimesEntity.endTime)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="event-times-habit">
                  <Translate contentKey="routineveeApp.eventTimes.habit">Habit</Translate>
                </Label>
                <AvInput id="event-times-habit" type="select" className="form-control" name="habitId">
                  <option value="" key="0" />
                  {habits
                    ? habits.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/event-times" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  habits: storeState.habit.entities,
  eventTimesEntity: storeState.eventTimes.entity,
  loading: storeState.eventTimes.loading,
  updating: storeState.eventTimes.updating,
  updateSuccess: storeState.eventTimes.updateSuccess
});

const mapDispatchToProps = {
  getHabits,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EventTimesUpdate);
