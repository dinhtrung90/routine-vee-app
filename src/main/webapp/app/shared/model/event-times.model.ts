import { Moment } from 'moment';

export interface IEventTimes {
  id?: number;
  dayOfWeek?: number;
  startTime?: Moment;
  endTime?: Moment;
  habitId?: number;
}

export const defaultValue: Readonly<IEventTimes> = {};
