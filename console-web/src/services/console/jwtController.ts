// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/.well-known/jwks.json */
export async function publicKey(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/v1/.well-known/jwks.json', {
    method: 'GET',
    ...(options || {}),
  });
}
