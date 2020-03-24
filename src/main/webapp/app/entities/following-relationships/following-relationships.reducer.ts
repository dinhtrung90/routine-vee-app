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

import { IFollowingRelationships, defaultValue } from 'app/shared/model/following-relationships.model';

export const ACTION_TYPES = {
  FETCH_FOLLOWINGRELATIONSHIPS_LIST: 'followingRelationships/FETCH_FOLLOWINGRELATIONSHIPS_LIST',
  FETCH_FOLLOWINGRELATIONSHIPS: 'followingRelationships/FETCH_FOLLOWINGRELATIONSHIPS',
  CREATE_FOLLOWINGRELATIONSHIPS: 'followingRelationships/CREATE_FOLLOWINGRELATIONSHIPS',
  UPDATE_FOLLOWINGRELATIONSHIPS: 'followingRelationships/UPDATE_FOLLOWINGRELATIONSHIPS',
  DELETE_FOLLOWINGRELATIONSHIPS: 'followingRelationships/DELETE_FOLLOWINGRELATIONSHIPS',
  RESET: 'followingRelationships/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFollowingRelationships>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FollowingRelationshipsState = Readonly<typeof initialState>;

// Reducer

export default (state: FollowingRelationshipsState = initialState, action): FollowingRelationshipsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FOLLOWINGRELATIONSHIPS):
    case REQUEST(ACTION_TYPES.UPDATE_FOLLOWINGRELATIONSHIPS):
    case REQUEST(ACTION_TYPES.DELETE_FOLLOWINGRELATIONSHIPS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS):
    case FAILURE(ACTION_TYPES.CREATE_FOLLOWINGRELATIONSHIPS):
    case FAILURE(ACTION_TYPES.UPDATE_FOLLOWINGRELATIONSHIPS):
    case FAILURE(ACTION_TYPES.DELETE_FOLLOWINGRELATIONSHIPS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FOLLOWINGRELATIONSHIPS):
    case SUCCESS(ACTION_TYPES.UPDATE_FOLLOWINGRELATIONSHIPS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FOLLOWINGRELATIONSHIPS):
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

const apiUrl = 'api/following-relationships';

// Actions

export const getEntities: ICrudGetAllAction<IFollowingRelationships> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS_LIST,
    payload: axios.get<IFollowingRelationships>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFollowingRelationships> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FOLLOWINGRELATIONSHIPS,
    payload: axios.get<IFollowingRelationships>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFollowingRelationships> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FOLLOWINGRELATIONSHIPS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFollowingRelationships> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FOLLOWINGRELATIONSHIPS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFollowingRelationships> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FOLLOWINGRELATIONSHIPS,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
