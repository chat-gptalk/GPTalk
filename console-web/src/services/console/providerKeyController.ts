// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/provider/keys */
export async function getProviderKeys(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getProviderKeysParams,
  options?: { [key: string]: any },
) {
  return request<API.ProviderKeyResponse[]>('/api/v1/provider/keys', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /api/v1/provider/keys */
export async function createProviderKey(
  body: API.CreateProviderKeyRequest,
  options?: { [key: string]: any },
) {
  return request<API.ProviderKeyResponse>('/api/v1/provider/keys', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /api/v1/provider/keys */
export async function batchDeleteProviderKey(
  body: API.BatchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/api/v1/provider/keys', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PATCH /api/v1/provider/keys/${param0} */
export async function patchProviderKey(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.patchProviderKeyParams,
  body: API.PatchProviderKeyRequest,
  options?: { [key: string]: any },
) {
  const { providerKeyId: param0, ...queryParams } = params;
  return request<API.ProviderKeyResponse>(`/api/v1/provider/keys/${param0}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /api/v1/provider/keys/${param0}/reveal */
export async function getProviderKeyValue(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getProviderKeyValueParams,
  options?: { [key: string]: any },
) {
  const { providerKeyId: param0, ...queryParams } = params;
  return request<API.ProviderKeyValueResponse>(`/api/v1/provider/keys/${param0}/reveal`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
