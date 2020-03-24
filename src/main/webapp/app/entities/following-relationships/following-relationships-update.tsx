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
import { getEntity, updateEntity, createEntity, reset } from './following-relationships.reducer';
import { IFollowingRelationships } from 'app/shared/model/following-relationships.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFollowingRelationshipsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FollowingRelationshipsUpdate = (props: IFollowingRelationshipsUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [userFollowingId, setUserFollowingId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { followingRelationshipsEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/following-relationships');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dateFollowed = convertDateTimeToServer(values.dateFollowed);

    if (errors.length === 0) {
      const entity = {
        ...followingRelationshipsEntity,
        ...values
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
          <h2 id="routineveeApp.followingRelationships.home.createOrEditLabel">
            <Translate contentKey="routineveeApp.followingRelationships.home.createOrEditLabel">
              Create or edit a FollowingRelationships
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : followingRelationshipsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="following-relationships-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="following-relationships-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateFollowedLabel" for="following-relationships-dateFollowed">
                  <Translate contentKey="routineveeApp.followingRelationships.dateFollowed">Date Followed</Translate>
                </Label>
                <AvInput
                  id="following-relationships-dateFollowed"
                  type="datetime-local"
                  className="form-control"
                  name="dateFollowed"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.followingRelationshipsEntity.dateFollowed)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="actionUserIdLabel" for="following-relationships-actionUserId">
                  <Translate contentKey="routineveeApp.followingRelationships.actionUserId">Action User Id</Translate>
                </Label>
                <AvField
                  id="following-relationships-actionUserId"
                  type="string"
                  className="form-control"
                  name="actionUserId"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="following-relationships-user">
                  <Translate contentKey="routineveeApp.followingRelationships.user">User</Translate>
                </Label>
                <AvInput id="following-relationships-user" type="select" className="form-control" name="userId" required>
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="following-relationships-userFollowing">
                  <Translate contentKey="routineveeApp.followingRelationships.userFollowing">User Following</Translate>
                </Label>
                <AvInput id="following-relationships-userFollowing" type="select" className="form-control" name="userFollowingId" required>
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/following-relationships" replace color="info">
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
  followingRelationshipsEntity: storeState.followingRelationships.entity,
  loading: storeState.followingRelationships.loading,
  updating: storeState.followingRelationships.updating,
  updateSuccess: storeState.followingRelationships.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FollowingRelationshipsUpdate);
