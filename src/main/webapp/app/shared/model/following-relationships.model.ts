import { Moment } from 'moment';

export interface IFollowingRelationships {
  id?: number;
  dateFollowed?: Moment;
  actionUserId?: number;
  userLogin?: string;
  userId?: number;
  userFollowingLogin?: string;
  userFollowingId?: number;
}

export const defaultValue: Readonly<IFollowingRelationships> = {};
