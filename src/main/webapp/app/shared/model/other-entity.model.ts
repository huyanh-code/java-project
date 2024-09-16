export interface IOtherEntity {
  id?: number;
  name?: string;
  birthDate?: Date;
}

export class OtherEntity implements IOtherEntity {
  constructor(
    public id?: number,
    public name?: string,
    public birthDate?: Date,
  ) {}
}
