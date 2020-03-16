import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IReminder } from 'app/shared/model/reminder.model';
import { getEntities as getReminders } from 'app/entities/reminder/reminder.reducer';
import { getEntity, updateEntity, createEntity, reset } from './habit.reducer';
import { IHabit } from 'app/shared/model/habit.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHabitUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HabitUpdate = (props: IHabitUpdateProps) => {
  const [reminderId, setReminderId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { habitEntity, reminders, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/habit');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getReminders();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...habitEntity,
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
          <h2 id="routineveeApp.habit.home.createOrEditLabel">
            <Translate contentKey="routineveeApp.habit.home.createOrEditLabel">Create or edit a Habit</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : habitEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="habit-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="habit-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="habit-name">
                  <Translate contentKey="routineveeApp.habit.name">Name</Translate>
                </Label>
                <AvField
                  id="habit-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="habit-type">
                  <Translate contentKey="routineveeApp.habit.type">Type</Translate>
                </Label>
                <AvInput id="habit-type" type="select" className="form-control" name="type" value={(!isNew && habitEntity.type) || 'BUILD'}>
                  <option value="BUILD">{translate('routineveeApp.HabitType.BUILD')}</option>
                  <option value="QUIT">{translate('routineveeApp.HabitType.QUIT')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="goalPeriodLabel" for="habit-goalPeriod">
                  <Translate contentKey="routineveeApp.habit.goalPeriod">Goal Period</Translate>
                </Label>
                <AvInput
                  id="habit-goalPeriod"
                  type="select"
                  className="form-control"
                  name="goalPeriod"
                  value={(!isNew && habitEntity.goalPeriod) || 'DAILY'}
                >
                  <option value="DAILY">{translate('routineveeApp.Period.DAILY')}</option>
                  <option value="WEEKLY">{translate('routineveeApp.Period.WEEKLY')}</option>
                  <option value="MONTHLY">{translate('routineveeApp.Period.MONTHLY')}</option>
                  <option value="YEARLY">{translate('routineveeApp.Period.YEARLY')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="completionGoalLabel" for="habit-completionGoal">
                  <Translate contentKey="routineveeApp.habit.completionGoal">Completion Goal</Translate>
                </Label>
                <AvField
                  id="habit-completionGoal"
                  type="string"
                  className="form-control"
                  name="completionGoal"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isGroupTrackingLabel">
                  <AvInput id="habit-isGroupTracking" type="checkbox" className="form-check-input" name="isGroupTracking" />
                  <Translate contentKey="routineveeApp.habit.isGroupTracking">Is Group Tracking</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="noteTextLabel" for="habit-noteText">
                  <Translate contentKey="routineveeApp.habit.noteText">Note Text</Translate>
                </Label>
                <AvField id="habit-noteText" type="text" name="noteText" />
              </AvGroup>
              <AvGroup>
                <Label id="motivateTextLabel" for="habit-motivateText">
                  <Translate contentKey="routineveeApp.habit.motivateText">Motivate Text</Translate>
                </Label>
                <AvField id="habit-motivateText" type="text" name="motivateText" />
              </AvGroup>
              <AvGroup check>
                <Label id="isReminderLabel">
                  <AvInput id="habit-isReminder" type="checkbox" className="form-check-input" name="isReminder" />
                  <Translate contentKey="routineveeApp.habit.isReminder">Is Reminder</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="habit-reminder">
                  <Translate contentKey="routineveeApp.habit.reminder">Reminder</Translate>
                </Label>
                <AvInput id="habit-reminder" type="select" className="form-control" name="reminderId">
                  <option value="" key="0" />
                  {reminders
                    ? reminders.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/habit" replace color="info">
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
  reminders: storeState.reminder.entities,
  habitEntity: storeState.habit.entity,
  loading: storeState.habit.loading,
  updating: storeState.habit.updating,
  updateSuccess: storeState.habit.updateSuccess
});

const mapDispatchToProps = {
  getReminders,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HabitUpdate);
