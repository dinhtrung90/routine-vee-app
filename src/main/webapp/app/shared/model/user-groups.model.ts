import { Moment } from 'moment';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IUserGroups {
  id?: number;
  name?: string;
  avataGroupUrl?: string;
  createAt?: Moment;
  userProfiles?: IUserProfile[];
}

export const defaultValue: Readonly<IUserGroups> = {};
