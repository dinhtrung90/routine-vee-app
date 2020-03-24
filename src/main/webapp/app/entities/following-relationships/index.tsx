import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FollowingRelationships from './following-relationships';
import FollowingRelationshipsDetail from './following-relationships-detail';
import FollowingRelationshipsUpdate from './following-relationships-update';
import FollowingRelationshipsDeleteDialog from './following-relationships-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FollowingRelationshipsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FollowingRelationshipsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FollowingRelationshipsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FollowingRelationshipsDetail} />
      <ErrorBoundaryRoute path={match.url} component={FollowingRelationships} />
    </Switch>
  </>
);

export default Routes;
