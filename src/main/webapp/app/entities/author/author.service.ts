import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IAuthor } from '@/shared/model/author.model';

const baseApiUrl = 'api/authors';

export default class AuthorService {
  public find(id: number): Promise<IAuthor> {
    return new Promise<IAuthor>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IAuthor): Promise<IAuthor> {
    return new Promise<IAuthor>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IAuthor, image?: any): Promise<IAuthor> {
    return new Promise<IAuthor>((resolve, reject) => {
      console.log('chuan bi update voi file', image);

      const formData = new FormData();
      formData.append('id', entity.id);
      formData.append('name', entity.name);
      formData.append('birthDate', entity.birthDate);
      formData.append('image', image);

      axios
        .put(`${baseApiUrl}/${entity.id}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IAuthor): Promise<IAuthor> {
    return new Promise<IAuthor>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
