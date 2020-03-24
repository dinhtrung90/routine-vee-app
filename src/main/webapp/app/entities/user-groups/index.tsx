import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserGroups from './user-groups';
import UserGroupsDetail from './user-groups-detail';
import UserGroupsUpdate from './user-groups-update';
import UserGroupsDeleteDialog from './user-groups-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserGroupsDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserGroupsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserGroupsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserGroupsDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserGroups} />
    </Switch>
  </>
);

export default Routes;
