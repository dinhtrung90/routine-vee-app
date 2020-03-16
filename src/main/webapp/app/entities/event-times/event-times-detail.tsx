import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './event-times.reducer';
import { IEventTimes } from 'app/shared/model/event-times.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEventTimesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EventTimesDetail = (props: IEventTimesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { eventTimesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.eventTimes.detail.title">EventTimes</Translate> [<b>{eventTimesEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="dayOfWeek">
              <Translate contentKey="routineveeApp.eventTimes.dayOfWeek">Day Of Week</Translate>
            </span>
          </dt>
          <dd>{eventTimesEntity.dayOfWeek}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="routineveeApp.eventTimes.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={eventTimesEntity.startTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="routineveeApp.eventTimes.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={eventTimesEntity.endTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <Translate contentKey="routineveeApp.eventTimes.habit">Habit</Translate>
          </dt>
          <dd>{eventTimesEntity.habitId ? eventTimesEntity.habitId : ''}</dd>
        </dl>
        <Button tag={Link} to="/event-times" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/event-times/${eventTimesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ eventTimes }: IRootState) => ({
  eventTimesEntity: eventTimes.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EventTimesDetail);
