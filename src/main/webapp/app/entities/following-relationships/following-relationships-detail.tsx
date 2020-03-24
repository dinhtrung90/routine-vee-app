import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './following-relationships.reducer';
import { IFollowingRelationships } from 'app/shared/model/following-relationships.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFollowingRelationshipsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FollowingRelationshipsDetail = (props: IFollowingRelationshipsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { followingRelationshipsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.followingRelationships.detail.title">FollowingRelationships</Translate> [
          <b>{followingRelationshipsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="dateFollowed">
              <Translate contentKey="routineveeApp.followingRelationships.dateFollowed">Date Followed</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={followingRelationshipsEntity.dateFollowed} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="actionUserId">
              <Translate contentKey="routineveeApp.followingRelationships.actionUserId">Action User Id</Translate>
            </span>
          </dt>
          <dd>{followingRelationshipsEntity.actionUserId}</dd>
          <dt>
            <Translate contentKey="routineveeApp.followingRelationships.user">User</Translate>
          </dt>
          <dd>{followingRelationshipsEntity.userLogin ? followingRelationshipsEntity.userLogin : ''}</dd>
          <dt>
            <Translate contentKey="routineveeApp.followingRelationships.userFollowing">User Following</Translate>
          </dt>
          <dd>{followingRelationshipsEntity.userFollowingLogin ? followingRelationshipsEntity.userFollowingLogin : ''}</dd>
        </dl>
        <Button tag={Link} to="/following-relationships" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/following-relationships/${followingRelationshipsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ followingRelationships }: IRootState) => ({
  followingRelationshipsEntity: followingRelationships.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FollowingRelationshipsDetail);
