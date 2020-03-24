import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './user-groups.reducer';
import { IUserGroups } from 'app/shared/model/user-groups.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserGroupsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserGroupsUpdate = (props: IUserGroupsUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { userGroupsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-groups');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.createAt = convertDateTimeToServer(values.createAt);

    if (errors.length === 0) {
      const entity = {
        ...userGroupsEntity,
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
          <h2 id="routineveeApp.userGroups.home.createOrEditLabel">
            <Translate contentKey="routineveeApp.userGroups.home.createOrEditLabel">Create or edit a UserGroups</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userGroupsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-groups-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="user-groups-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="user-groups-name">
                  <Translate contentKey="routineveeApp.userGroups.name">Name</Translate>
                </Label>
                <AvField id="user-groups-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="avataGroupUrlLabel" for="user-groups-avataGroupUrl">
                  <Translate contentKey="routineveeApp.userGroups.avataGroupUrl">Avata Group Url</Translate>
                </Label>
                <AvField id="user-groups-avataGroupUrl" type="text" name="avataGroupUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="createAtLabel" for="user-groups-createAt">
                  <Translate contentKey="routineveeApp.userGroups.createAt">Create At</Translate>
                </Label>
                <AvInput
                  id="user-groups-createAt"
                  type="datetime-local"
                  className="form-control"
                  name="createAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.userGroupsEntity.createAt)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/user-groups" replace color="info">
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
  userGroupsEntity: storeState.userGroups.entity,
  loading: storeState.userGroups.loading,
  updating: storeState.userGroups.updating,
  updateSuccess: storeState.userGroups.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserGroupsUpdate);