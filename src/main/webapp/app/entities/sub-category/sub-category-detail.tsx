import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './sub-category.reducer';
import { ISubCategory } from 'app/shared/model/sub-category.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubCategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubCategoryDetail = (props: ISubCategoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subCategoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="routineveeApp.subCategory.detail.title">SubCategory</Translate> [<b>{subCategoryEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="routineveeApp.subCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="routineveeApp.subCategory.code">Code</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="routineveeApp.subCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{subCategoryEntity.description}</dd>
          <dt>
            <Translate contentKey="routineveeApp.subCategory.category">Category</Translate>
          </dt>
          <dd>{subCategoryEntity.categoryId ? subCategoryEntity.categoryId : ''}</dd>
        </dl>
        <Button tag={Link} to="/sub-category" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-category/${subCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subCategory }: IRootState) => ({
  subCategoryEntity: subCategory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubCategoryDetail);
