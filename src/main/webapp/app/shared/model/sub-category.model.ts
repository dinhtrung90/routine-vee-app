export interface ISubCategory {
  id?: number;
  name?: string;
  code?: string;
  description?: string;
  categoryId?: number;
}

export const defaultValue: Readonly<ISubCategory> = {};
