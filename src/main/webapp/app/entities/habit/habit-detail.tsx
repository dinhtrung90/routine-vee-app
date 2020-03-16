import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './habit.reducer';
import { IHabit } from 'app/shared/model/habit.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHabitDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HabitDetail = (props: IHabitDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { habitEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.habit.detail.title">Habit</Translate> [<b>{habitEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="routineveeApp.habit.name">Name</Translate>
            </span>
          </dt>
          <dd>{habitEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="routineveeApp.habit.type">Type</Translate>
            </span>
          </dt>
          <dd>{habitEntity.type}</dd>
          <dt>
            <span id="goalPeriod">
              <Translate contentKey="routineveeApp.habit.goalPeriod">Goal Period</Translate>
            </span>
          </dt>
          <dd>{habitEntity.goalPeriod}</dd>
          <dt>
            <span id="completionGoal">
              <Translate contentKey="routineveeApp.habit.completionGoal">Completion Goal</Translate>
            </span>
          </dt>
          <dd>{habitEntity.completionGoal}</dd>
          <dt>
            <span id="isGroupTracking">
              <Translate contentKey="routineveeApp.habit.isGroupTracking">Is Group Tracking</Translate>
            </span>
          </dt>
          <dd>{habitEntity.isGroupTracking ? 'true' : 'false'}</dd>
          <dt>
            <span id="noteText">
              <Translate contentKey="routineveeApp.habit.noteText">Note Text</Translate>
            </span>
          </dt>
          <dd>{habitEntity.noteText}</dd>
          <dt>
            <span id="motivateText">
              <Translate contentKey="routineveeApp.habit.motivateText">Motivate Text</Translate>
            </span>
          </dt>
          <dd>{habitEntity.motivateText}</dd>
          <dt>
            <span id="isReminder">
              <Translate contentKey="routineveeApp.habit.isReminder">Is Reminder</Translate>
            </span>
          </dt>
          <dd>{habitEntity.isReminder ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="routineveeApp.habit.reminder">Reminder</Translate>
          </dt>
          <dd>{habitEntity.reminderId ? habitEntity.reminderId : ''}</dd>
        </dl>
        <Button tag={Link} to="/habit" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/habit/${habitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ habit }: IRootState) => ({
  habitEntity: habit.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HabitDetail);
