import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-groups.reducer';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserGroupsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserGroupsDetail = (props: IUserGroupsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { userGroupsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.userGroups.detail.title">UserGroups</Translate> [<b>{userGroupsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="routineveeApp.userGroups.name">Name</Translate>
            </span>
          </dt>
          <dd>{userGroupsEntity.name}</dd>
          <dt>
            <span id="avataGroupUrl">
              <Translate contentKey="routineveeApp.userGroups.avataGroupUrl">Avata Group Url</Translate>
            </span>
          </dt>
          <dd>{userGroupsEntity.avataGroupUrl}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="routineveeApp.userGroups.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={userGroupsEntity.createAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <Translate contentKey="routineveeApp.userGroups.userProfile">User Profile</Translate>
          </dt>
          <dd>
            {userGroupsEntity.userProfiles
              ? userGroupsEntity.userProfiles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {i === userGroupsEntity.userProfiles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-groups" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-groups/${userGroupsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ userGroups }: IRootState) => ({
  userGroupsEntity: userGroups.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserGroupsDetail);
