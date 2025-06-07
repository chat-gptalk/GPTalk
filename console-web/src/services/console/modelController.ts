// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/models */
export async function getModels(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getModelsParams,
  options?: { [key: string]: any },
) {
  return request<API.ModelResponse[]>('/api/v1/models', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /api/v1/models */
export async function createModel(body: API.CreateModelRequest, options?: { [key: string]: any }) {
  return request<API.ModelResponse>('/api/v1/models', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /api/v1/models */
export async function batchDeleteModel(
  body: API.BatchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/v1/models', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PATCH /api/v1/models/${param0} */
export async function patchModel(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.patchModelParams,
  body: API.PatchModelRequest,
  options?: { [key: string]: any },
) {
  const { modelId: param0, ...queryParams } = params;
  return request<API.ModelResponse>(`/api/v1/models/${param0}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}
