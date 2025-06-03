// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/virtual-models */
export async function getModels(options?: { [key: string]: any }) {
  return request<API.VirtualModelResponse[]>('/api/v1/virtual-models', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /api/v1/virtual-models */
export async function createModel(
  body: API.CreateVirtualModelRequest,
  options?: { [key: string]: any },
) {
  return request<API.VirtualModelResponse>('/api/v1/virtual-models', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /api/v1/virtual-models */
export async function batchDelete(body: API.BatchDeleteRequest, options?: { [key: string]: any }) {
  return request<any>('/api/v1/virtual-models', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
