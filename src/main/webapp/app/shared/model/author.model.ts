export interface IAuthor {
  id?: number;
  name?: string | null;
  birthDate?: Date | null;
  imageAuthor?: Blob | null;
}

export class Author implements IAuthor {
  constructor(
    public id?: number,
    public name?: string | null,
    public birthDate?: Date | null,
    public imageAuthor?: Blob | null,
  ) {}
}
