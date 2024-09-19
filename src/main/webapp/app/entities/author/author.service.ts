import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IAuthor } from '@/shared/model/author.model';

const baseApiUrl = 'api/authors';

export class AuthorSearchCondition {
  authorName?: String;
}

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

  public search(name: String, paginationQuery?: any): Promise<any> {
    return new Promise<IAuthor[]>((resolve, reject) => {
      // Kiểm tra nếu 'name' không null và có giá trị
      let nameQuery = '';
      if (name) {
        nameQuery = `name.contains=${name}`;
      }
      axios
        .get(`${baseApiUrl}?${nameQuery}&${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res.data); // Trả về danh sách các tác giả
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(searchCondition?: AuthorSearchCondition, paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const queryParameters: String[] = [];
      if (searchCondition) {
        if (searchCondition.authorName) {
          queryParameters.push(`name.contains=${searchCondition.authorName}`);
        }
      }
      const q = queryParameters.join('&'); // name.contains=aaa&authorname.contains=bbb

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

  public update(entity: IAuthor): Promise<IAuthor> {
    return new Promise<IAuthor>((resolve, reject) => {
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
