declare namespace API {
  type batchDeleteProviderModelParams = {
    providerId: string;
  };

  type BatchDeleteRequest = {
    ids: string[];
  };

  type ConsoleAuthenticatedUser = {
    userId?: string;
    username?: string;
    tenantId?: string;
    roles?: string[];
  };

  type CreateKeyRequest = {
    name: string;
  };

  type CreateKeyResponse = {
    apiKeyId: string;
    key: string;
    name: string;
    userId: string;
    tenantId: string;
    allowedModels?: string[];
    status: number;
    expireAt?: string;
    createdAt: string;
    updatedAt: string;
  };

  type createProviderModelParams = {
    providerId: string;
  };

  type CreateProviderModelRequest = {
    name: string;
    features: (
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
    )[];
  };

  type CreateProviderRequest = {
    name: string;
    baseUrl: string;
    description?: string;
    sdkClientClass: string;
    extraConfig?: Record<string, any>;
  };

  type CreateVirtualModelRequest = {
    name?: string;
    description?: string;
    modelIds?: string[];
  };

  type getProviderModelsParams = {
    providerId: string;
  };

  type getProviderParams = {
    providerId: string;
  };

  type LoginRequest = {
    username: string;
    password: string;
  };

  type LoginResponse = {
    accessToken: string;
    refreshToken: string;
    expiresIn: number;
    tokenType: string;
  };

  type MappedModelResponse = {
    model: ModelResponse;
    priority: number;
    weight: number;
  };

  type ModelResponse = {
    modelId: string;
    provider: ProviderResponse;
    name: string;
    description?: string;
    features: (
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
    )[];
    contextLength: number;
    status:
      | 'HEALTHY'
      | 'AUTH_FAILED'
      | 'RATE_LIMITED'
      | 'INVALID_CONFIG'
      | 'UNREACHABLE'
      | 'UNKNOWN_ERROR';
    maxOutputTokens: number;
    enabled: boolean;
    defaultParams?: Record<string, any>;
    createdAt: string;
    updatedAt: string;
  };

  type ProviderResponse = {
    providerId: string;
    name: string;
    baseUrl: string;
    description?: string;
    sdkClientClass: string;
    system: boolean;
    enabled: boolean;
    extraConfig?: Record<string, any>;
    modelCount: number;
    createdAt: string;
    updatedAt: string;
  };

  type RegisterRequest = {
    username: string;
    password: string;
  };

  type RegisterResponse = {
    username: string;
    userId: string;
    password: string;
    status: number;
    tenantId: string;
    createdAt: string;
    updatedAt: string;
  };

  type TreeNode = {
    title: string;
    value: string;
    key: string;
    selectable: boolean;
    children: any[];
  };

  type VirtualModelResponse = {
    name: string;
    virtualModelId: string;
    enabled: boolean;
    models: MappedModelResponse[];
    createdAt: string;
    updatedAt: string;
  };
}
