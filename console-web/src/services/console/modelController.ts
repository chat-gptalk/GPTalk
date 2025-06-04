// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /api/v1/models/features */
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
  >('/api/v1/models/features', {
    method: 'GET',
    ...(options || {}),
  });
}
