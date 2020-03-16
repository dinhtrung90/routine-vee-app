import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Category from './category';
import SubCategory from './sub-category';
import Habit from './habit';
import Reminder from './reminder';
import EventTimes from './event-times';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}sub-category`} component={SubCategory} />
      <ErrorBoundaryRoute path={`${match.url}habit`} component={Habit} />
      <ErrorBoundaryRoute path={`${match.url}reminder`} component={Reminder} />
      <ErrorBoundaryRoute path={`${match.url}event-times`} component={EventTimes} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
