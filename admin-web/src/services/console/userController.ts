// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/users/me */
export async function me(options?: { [key: string]: any }) {
  return request<API.ConsoleAuthenticatedUser>('/api/v1/users/me', {
    method: 'GET',
    ...(options || {}),
  });
}
