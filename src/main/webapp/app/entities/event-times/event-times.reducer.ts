import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEventTimes, defaultValue } from 'app/shared/model/event-times.model';

export const ACTION_TYPES = {
  FETCH_EVENTTIMES_LIST: 'eventTimes/FETCH_EVENTTIMES_LIST',
  FETCH_EVENTTIMES: 'eventTimes/FETCH_EVENTTIMES',
  CREATE_EVENTTIMES: 'eventTimes/CREATE_EVENTTIMES',
  UPDATE_EVENTTIMES: 'eventTimes/UPDATE_EVENTTIMES',
  DELETE_EVENTTIMES: 'eventTimes/DELETE_EVENTTIMES',
  RESET: 'eventTimes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEventTimes>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EventTimesState = Readonly<typeof initialState>;

// Reducer

export default (state: EventTimesState = initialState, action): EventTimesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EVENTTIMES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EVENTTIMES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EVENTTIMES):
    case REQUEST(ACTION_TYPES.UPDATE_EVENTTIMES):
    case REQUEST(ACTION_TYPES.DELETE_EVENTTIMES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EVENTTIMES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EVENTTIMES):
    case FAILURE(ACTION_TYPES.CREATE_EVENTTIMES):
    case FAILURE(ACTION_TYPES.UPDATE_EVENTTIMES):
    case FAILURE(ACTION_TYPES.DELETE_EVENTTIMES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EVENTTIMES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_EVENTTIMES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EVENTTIMES):
    case SUCCESS(ACTION_TYPES.UPDATE_EVENTTIMES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EVENTTIMES):
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

const apiUrl = 'api/event-times';

// Actions

export const getEntities: ICrudGetAllAction<IEventTimes> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EVENTTIMES_LIST,
    payload: axios.get<IEventTimes>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEventTimes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EVENTTIMES,
    payload: axios.get<IEventTimes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEventTimes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EVENTTIMES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEventTimes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EVENTTIMES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEventTimes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EVENTTIMES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
