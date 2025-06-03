// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 POST /api/v1/keys */
export async function createKey(body: API.CreateKeyRequest, options?: { [key: string]: any }) {
  return request<API.CreateKeyResponse>('/api/v1/keys', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
