import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IBook } from '@/shared/model/book.model';

const baseApiUrl = 'api/books';

export class BookSearchCondition {
  title?: String;
  authorName?: String;
}

export default class BookService {
  public find(id: number): Promise<IBook> {
    return new Promise<IBook>((resolve, reject) => {
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

  public retrieve(searchConditions?: BookSearchCondition, paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const queryParameters: String[] = [];
      if (searchConditions) {
        if (searchConditions.title) {
          queryParameters.push(`title.contains=${searchConditions.title}`);
        }
        if (searchConditions.authorName) {
          queryParameters.push(`authorName.contains=${searchConditions.authorName}`);
        }
      }

      const q = queryParameters.join('&');

      axios
        .get(`${baseApiUrl}?${q}&${buildPaginationQueryOpts(paginationQuery)}`)
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

  public create(entity: IBook): Promise<IBook> {
    return new Promise<IBook>((resolve, reject) => {
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

  public update(entity: IBook): Promise<IBook> {
    return new Promise<IBook>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IBook): Promise<IBook> {
    return new Promise<IBook>((resolve, reject) => {
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
