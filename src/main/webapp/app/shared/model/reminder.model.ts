import { Moment } from 'moment';

export interface IReminder {
  id?: number;
  reminderText?: string;
  date?: Moment;
}

export const defaultValue: Readonly<IReminder> = {};
