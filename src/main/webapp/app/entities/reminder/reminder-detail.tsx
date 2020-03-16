import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reminder.reducer';
import { IReminder } from 'app/shared/model/reminder.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReminderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReminderDetail = (props: IReminderDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { reminderEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.reminder.detail.title">Reminder</Translate> [<b>{reminderEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="reminderText">
              <Translate contentKey="routineveeApp.reminder.reminderText">Reminder Text</Translate>
            </span>
          </dt>
          <dd>{reminderEntity.reminderText}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="routineveeApp.reminder.date">Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={reminderEntity.date} type="date" format={APP_DATE_FORMAT} />
          </dd>
        </dl>
        <Button tag={Link} to="/reminder" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reminder/${reminderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ reminder }: IRootState) => ({
  reminderEntity: reminder.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReminderDetail);
