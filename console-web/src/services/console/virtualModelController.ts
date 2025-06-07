// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/virtual-models */
export async function getVirtualModels(options?: { [key: string]: any }) {
  return request<API.VirtualModelResponse[]>('/api/v1/virtual-models', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /api/v1/virtual-models */
export async function createVirtualModel(
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
export async function batchDeleteVirtualModel(
  body: API.BatchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/v1/virtual-models', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PATCH /api/v1/virtual-models/${param0} */
export async function patchVirtualModel(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.patchVirtualModelParams,
  body: API.PatchVirtualModelRequest,
  options?: { [key: string]: any },
) {
  const { virtualModelId: param0, ...queryParams } = params;
  return request<API.VirtualModelResponse>(`/api/v1/virtual-models/${param0}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}
