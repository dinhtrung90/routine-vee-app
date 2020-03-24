import { IUserGroups } from 'app/shared/model/user-groups.model';

export interface IUserProfile {
  id?: number;
  userKey?: string;
  fullName?: string;
  avartarUrl?: string;
  coverURl?: string;
  userLogin?: string;
  userId?: number;
  userGroups?: IUserGroups[];
}

export const defaultValue: Readonly<IUserProfile> = {};
