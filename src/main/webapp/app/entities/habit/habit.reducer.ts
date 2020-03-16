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

import { IHabit, defaultValue } from 'app/shared/model/habit.model';

export const ACTION_TYPES = {
  FETCH_HABIT_LIST: 'habit/FETCH_HABIT_LIST',
  FETCH_HABIT: 'habit/FETCH_HABIT',
  CREATE_HABIT: 'habit/CREATE_HABIT',
  UPDATE_HABIT: 'habit/UPDATE_HABIT',
  DELETE_HABIT: 'habit/DELETE_HABIT',
  RESET: 'habit/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHabit>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HabitState = Readonly<typeof initialState>;

// Reducer

export default (state: HabitState = initialState, action): HabitState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HABIT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HABIT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HABIT):
    case REQUEST(ACTION_TYPES.UPDATE_HABIT):
    case REQUEST(ACTION_TYPES.DELETE_HABIT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HABIT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HABIT):
    case FAILURE(ACTION_TYPES.CREATE_HABIT):
    case FAILURE(ACTION_TYPES.UPDATE_HABIT):
    case FAILURE(ACTION_TYPES.DELETE_HABIT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HABIT_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_HABIT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HABIT):
    case SUCCESS(ACTION_TYPES.UPDATE_HABIT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HABIT):
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

const apiUrl = 'api/habits';

// Actions

export const getEntities: ICrudGetAllAction<IHabit> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HABIT_LIST,
    payload: axios.get<IHabit>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHabit> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HABIT,
    payload: axios.get<IHabit>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHabit> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HABIT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IHabit> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HABIT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHabit> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HABIT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
