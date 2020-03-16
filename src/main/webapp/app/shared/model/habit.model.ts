import { IEventTimes } from 'app/shared/model/event-times.model';
import { HabitType } from 'app/shared/model/enumerations/habit-type.model';
import { Period } from 'app/shared/model/enumerations/period.model';

export interface IHabit {
  id?: number;
  name?: string;
  type?: HabitType;
  goalPeriod?: Period;
  completionGoal?: number;
  isGroupTracking?: boolean;
  noteText?: string;
  motivateText?: string;
  isReminder?: boolean;
  reminderId?: number;
  eventTimes?: IEventTimes[];
}

export const defaultValue: Readonly<IHabit> = {
  isGroupTracking: false,
  isReminder: false
};
