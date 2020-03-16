import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EventTimes from './event-times';
import EventTimesDetail from './event-times-detail';
import EventTimesUpdate from './event-times-update';
import EventTimesDeleteDialog from './event-times-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EventTimesDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EventTimesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EventTimesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventTimesDetail} />
      <ErrorBoundaryRoute path={match.url} component={EventTimes} />
    </Switch>
  </>
);

export default Routes;
