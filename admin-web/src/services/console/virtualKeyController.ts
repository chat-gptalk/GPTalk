// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/virtual-keys */
export async function getVirtualKeys(options?: { [key: string]: any }) {
  return request<API.VirtualKeyResponse[]>('/api/v1/virtual-keys', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /api/v1/virtual-keys */
export async function createVirtualKey(
  body: API.CreateVirtualKeyRequest,
  options?: { [key: string]: any },
) {
  return request<API.VirtualKeyResponse>('/api/v1/virtual-keys', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /api/v1/virtual-keys */
export async function batchDeleteVirtualKey(
  body: API.BatchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/v1/virtual-keys', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
