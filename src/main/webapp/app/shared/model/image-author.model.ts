import { type IAuthor } from '@/shared/model/author.model';

export interface IImageAuthor {
  id?: number;
  imageAuthorContentType?: string | null;
  imageAuthor?: string | null;
  author_id?: IAuthor | null;
}

export class ImageAuthor implements IImageAuthor {
  constructor(
    public id?: number,
    public imageAuthorContentType?: string | null,
    public imageAuthor?: string | null,
    public author_id?: IAuthor | null,
  ) {}
}
