import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/category">
      <Translate contentKey="global.menu.entities.category" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/sub-category">
      <Translate contentKey="global.menu.entities.subCategory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/habit">
      <Translate contentKey="global.menu.entities.habit" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/reminder">
      <Translate contentKey="global.menu.entities.reminder" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/event-times">
      <Translate contentKey="global.menu.entities.eventTimes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-profile">
      <Translate contentKey="global.menu.entities.userProfile" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/following-relationships">
      <Translate contentKey="global.menu.entities.followingRelationships" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-groups">
      <Translate contentKey="global.menu.entities.userGroups" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
