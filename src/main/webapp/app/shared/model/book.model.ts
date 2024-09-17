import { type IAuthor } from '@/shared/model/author.model';

export interface IBook {
  id?: number;
  title?: string;
  description?: string;
  publicationDate?: Date;
  price?: number;
  author?: IAuthor | null;
}

export class Book implements IBook {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public publicationDate?: Date,
    public price?: number,
    public author?: IAuthor | null,
  ) {}
}
