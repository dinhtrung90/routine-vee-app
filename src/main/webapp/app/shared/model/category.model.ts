import { ISubCategory } from 'app/shared/model/sub-category.model';

export interface ICategory {
  id?: number;
  name?: string;
  code?: string;
  description?: string;
  thumbUrl?: string;
  subCategories?: ISubCategory[];
}

export const defaultValue: Readonly<ICategory> = {};
