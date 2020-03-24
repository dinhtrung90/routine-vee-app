import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserGroups, defaultValue } from 'app/shared/model/user-groups.model';

export const ACTION_TYPES = {
  FETCH_USERGROUPS_LIST: 'userGroups/FETCH_USERGROUPS_LIST',
  FETCH_USERGROUPS: 'userGroups/FETCH_USERGROUPS',
  CREATE_USERGROUPS: 'userGroups/CREATE_USERGROUPS',
  UPDATE_USERGROUPS: 'userGroups/UPDATE_USERGROUPS',
  DELETE_USERGROUPS: 'userGroups/DELETE_USERGROUPS',
  RESET: 'userGroups/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserGroups>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserGroupsState = Readonly<typeof initialState>;

// Reducer

export default (state: UserGroupsState = initialState, action): UserGroupsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERGROUPS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERGROUPS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERGROUPS):
    case REQUEST(ACTION_TYPES.UPDATE_USERGROUPS):
    case REQUEST(ACTION_TYPES.DELETE_USERGROUPS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERGROUPS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERGROUPS):
    case FAILURE(ACTION_TYPES.CREATE_USERGROUPS):
    case FAILURE(ACTION_TYPES.UPDATE_USERGROUPS):
    case FAILURE(ACTION_TYPES.DELETE_USERGROUPS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERGROUPS_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_USERGROUPS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERGROUPS):
    case SUCCESS(ACTION_TYPES.UPDATE_USERGROUPS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERGROUPS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/user-groups';

// Actions

export const getEntities: ICrudGetAllAction<IUserGroups> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERGROUPS_LIST,
    payload: axios.get<IUserGroups>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserGroups> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERGROUPS,
    payload: axios.get<IUserGroups>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserGroups> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERGROUPS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IUserGroups> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERGROUPS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserGroups> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERGROUPS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
