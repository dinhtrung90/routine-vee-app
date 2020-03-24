import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { getEntities as getUserGroups } from 'app/entities/user-groups/user-groups.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserProfileUpdate = (props: IUserProfileUpdateProps) => {
  const [idsuserGroups, setIdsuserGroups] = useState([]);
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { userProfileEntity, users, userGroups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-profile');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getUserGroups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...userProfileEntity,
        ...values,
        userGroups: mapIdList(values.userGroups)
      };
      entity.user = users[values.user];

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
          <h2 id="routineveeApp.userProfile.home.createOrEditLabel">
            <Translate contentKey="routineveeApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userProfileEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-profile-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="user-profile-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userKeyLabel" for="user-profile-userKey">
                  <Translate contentKey="routineveeApp.userProfile.userKey">User Key</Translate>
                </Label>
                <AvField
                  id="user-profile-userKey"
                  type="text"
                  name="userKey"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="fullNameLabel" for="user-profile-fullName">
                  <Translate contentKey="routineveeApp.userProfile.fullName">Full Name</Translate>
                </Label>
                <AvField id="user-profile-fullName" type="text" name="fullName" />
              </AvGroup>
              <AvGroup>
                <Label id="avartarUrlLabel" for="user-profile-avartarUrl">
                  <Translate contentKey="routineveeApp.userProfile.avartarUrl">Avartar Url</Translate>
                </Label>
                <AvField id="user-profile-avartarUrl" type="text" name="avartarUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="coverURlLabel" for="user-profile-coverURl">
                  <Translate contentKey="routineveeApp.userProfile.coverURl">Cover U Rl</Translate>
                </Label>
                <AvField id="user-profile-coverURl" type="text" name="coverURl" />
              </AvGroup>
              <AvGroup>
                <Label for="user-profile-user">
                  <Translate contentKey="routineveeApp.userProfile.user">User</Translate>
                </Label>
                <AvInput id="user-profile-user" type="select" className="form-control" name="userId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="user-profile-userGroups">
                  <Translate contentKey="routineveeApp.userProfile.userGroups">User Groups</Translate>
                </Label>
                <AvInput
                  id="user-profile-userGroups"
                  type="select"
                  multiple
                  className="form-control"
                  name="userGroups"
                  value={userProfileEntity.userGroups && userProfileEntity.userGroups.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {userGroups
                    ? userGroups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/user-profile" replace color="info">
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
  users: storeState.userManagement.users,
  userGroups: storeState.userGroups.entities,
  userProfileEntity: storeState.userProfile.entity,
  loading: storeState.userProfile.loading,
  updating: storeState.userProfile.updating,
  updateSuccess: storeState.userProfile.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getUserGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserProfileUpdate);
