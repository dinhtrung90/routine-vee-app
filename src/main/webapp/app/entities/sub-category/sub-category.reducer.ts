import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubCategory, defaultValue } from 'app/shared/model/sub-category.model';

export const ACTION_TYPES = {
  FETCH_SUBCATEGORY_LIST: 'subCategory/FETCH_SUBCATEGORY_LIST',
  FETCH_SUBCATEGORY: 'subCategory/FETCH_SUBCATEGORY',
  CREATE_SUBCATEGORY: 'subCategory/CREATE_SUBCATEGORY',
  UPDATE_SUBCATEGORY: 'subCategory/UPDATE_SUBCATEGORY',
  DELETE_SUBCATEGORY: 'subCategory/DELETE_SUBCATEGORY',
  RESET: 'subCategory/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubCategory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SubCategoryState = Readonly<typeof initialState>;

// Reducer

export default (state: SubCategoryState = initialState, action): SubCategoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBCATEGORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBCATEGORY):
    case REQUEST(ACTION_TYPES.UPDATE_SUBCATEGORY):
    case REQUEST(ACTION_TYPES.DELETE_SUBCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBCATEGORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBCATEGORY):
    case FAILURE(ACTION_TYPES.CREATE_SUBCATEGORY):
    case FAILURE(ACTION_TYPES.UPDATE_SUBCATEGORY):
    case FAILURE(ACTION_TYPES.DELETE_SUBCATEGORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBCATEGORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBCATEGORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBCATEGORY):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBCATEGORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBCATEGORY):
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

const apiUrl = 'api/sub-categories';

// Actions

export const getEntities: ICrudGetAllAction<ISubCategory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUBCATEGORY_LIST,
    payload: axios.get<ISubCategory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISubCategory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBCATEGORY,
    payload: axios.get<ISubCategory>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBCATEGORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBCATEGORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubCategory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBCATEGORY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
