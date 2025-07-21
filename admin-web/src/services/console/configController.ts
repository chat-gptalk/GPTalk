// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/config/model/features */
export async function getModelFeatures(options?: { [key: string]: any }) {
  return request<
    (
      | 'CHAT'
      | 'COMPLETION'
      | 'EMBEDDING'
      | 'VISION'
      | 'TOOL_CALLING'
      | 'MULTI_MODAL'
      | 'SPEECH'
      | 'CODE_INTERPRETER'
      | 'STREAMING'
      | 'JSON_MODE'
      | 'SYSTEM_PROMPT'
    )[]
  >('/api/v1/config/model/features', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /api/v1/config/model/status */
export async function getModelStatus(options?: { [key: string]: any }) {
  return request<
    (
      | 'HEALTHY'
      | 'PENDING'
      | 'AUTH_FAILED'
      | 'RATE_LIMITED'
      | 'INVALID_CONFIG'
      | 'UNREACHABLE'
      | 'UNKNOWN_ERROR'
    )[]
  >('/api/v1/config/model/status', {
    method: 'GET',
    ...(options || {}),
  });
}
