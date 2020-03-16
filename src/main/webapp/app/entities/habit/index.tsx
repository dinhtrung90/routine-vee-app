import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Habit from './habit';
import HabitDetail from './habit-detail';
import HabitUpdate from './habit-update';
import HabitDeleteDialog from './habit-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HabitDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HabitUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HabitUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HabitDetail} />
      <ErrorBoundaryRoute path={match.url} component={Habit} />
    </Switch>
  </>
);

export default Routes;
