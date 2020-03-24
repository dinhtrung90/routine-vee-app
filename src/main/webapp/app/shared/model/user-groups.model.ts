import { Moment } from 'moment';

export interface IUserGroups {
  id?: number;
  name?: string;
  avataGroupUrl?: string;
  createAt?: Moment;
}

export const defaultValue: Readonly<IUserGroups> = {};
