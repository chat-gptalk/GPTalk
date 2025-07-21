import axios, {AxiosRequestConfig, AxiosResponse} from 'axios';
import {history} from '@umijs/max';

const request = <T>(uri: string, options: any): Promise<T> => {
  return axios.request({
    url: uri,
    headers: options.headers,
    method: options.method,
    params: options.params,
    data: options.data,
  })
  .then(it => {
    if (it.status === 204) {
      return;
    }
    if (it.status === 200 || it.status === 201) {
      return it.data;
    }
    return Promise.reject({
      status: it.data.status,
      detail: it.data.detail
    })
  }).catch(e => {
    const status = e.response.status;
    if (uri !== '/api/v1/auth/login' && status === 401) {
      history.push('/auth/login')
      return;
    }
    if (status === 403) {
      history.push('/error/403')
      return;
    }
    return Promise.reject({
      status: e.response.data.status,
      detail: e.response.data.detail
    })
  });
};
export default request;