import {DeleteOutlined, EditOutlined, EllipsisOutlined, PlusOutlined, SettingOutlined} from '@ant-design/icons';
import {PageContainer} from '@ant-design/pro-components';
import {useIntl, useSearchParams, history} from '@umijs/max';
import {
  App,
  Avatar,
  Badge,
  Button,
  Card,
  ConfigProvider,
  Descriptions, Dropdown,
  Flex,
  List, MenuProps,
  Select, Space,
  Switch,
  Tag
} from 'antd';
import {
  getModels,
  createModel,
  batchDeleteModel,
  patchModel,
} from '@/services/console/modelController';
import {getProviders} from '@/services/console/providerController';
import {getModelStatus} from '@/services/console/configController';
import React, {useState} from 'react';
import {useRequest} from "ahooks";
import useStyles from './style.style';
import ModelModalForm from "@/pages/Model/components/ModelModalForm";
import {isNotPlaceholder, Placeholder, PlaceholderType} from "@/utils/placeholder";
import {Colors} from "@/constants";
import {hashColor} from "@/utils/color";
import {batchDeleteProviderKey} from "@/services/console/providerKeyController";

const AIService: React.FC = () => {
  const intl = useIntl();
  const {styles} = useStyles();

  const [searchParams] = useSearchParams();

  const [modelOpen, setModelOpen] = useState(false);
  const [providerId, setProviderId] = useState<string | null>(searchParams.get('providerId'));
  const [modelStatus, setModelStatus] = useState<string>();
  const [selectedData, setSelectedData] = useState<API.ModelResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');

  const {modal, message} = App.useApp();
  const {data: providers, loading: providerLoading} = useRequest(() => getProviders());
  const {data: allStatus} = useRequest(() => getModelStatus());


  const {data: models, loading, refresh} = useRequest((): Promise<API.ModelResponse[]> => {
    return getModels({providerId: providerId ?? undefined, status: modelStatus});
  }, {
    refreshDeps: [providerId, modelStatus]
  });
  if (!models) {
    return;
  }

  function getActions(item: API.ModelResponse) {
    const items: MenuProps['items'] = [
      {
        key: `del-${item.modelId}`,
        label: (
            <Space onClick={() => doDelete([item.modelId])}><DeleteOutlined style={{color: 'red'}}/> Delete</Space>
        ),
      },
    ];
    return [
      <EditOutlined key="edit" onClick={() => {
        setFormMode('edit');
        setSelectedData(item);
        setModelOpen(true);
      }}/>,
      <Switch value={item.enabled} onChange={(newVal) => {
        return patchModel({modelId: item.modelId}, {
          enabled: newVal,
        }).then((res) => {
          message.success(newVal ? "Model activated successfully" : "Model deactivated successfully");
          refresh();
        });
      }}/>,
      <Dropdown menu={{items}}>
        <EllipsisOutlined key="ellipsis"/>
      </Dropdown>
    ];
  }

  const dataSource: (PlaceholderType | API.ModelResponse)[] = [Placeholder, ...models];

  function getStatusColor(item: API.ModelResponse) {
    switch (item.status) {
      case "HEALTHY":
        return Colors.primaryColor;
      case "INVALID_CONFIG":
      case "RATE_LIMITED":
        return Colors.warning;
      case "UNREACHABLE":
      case "UNKNOWN_ERROR":
        return Colors.error;
      default:
        return Colors.gray;
    }
  }

  function doDelete(ids: string[]) {
    if (!ids || ids.length == 0) {
      return;
    }
    modal.confirm({
      title: 'Confirm',
      content: 'Are you sure you want to delete this?',
      onOk: () => batchDeleteModel({
        ids
      }).then(res => {
        message.success('Deleted successfully');
      }).catch(e => {
        message.error(e.detail || 'Delete failed');
      }).finally(() => {
        refresh();
      }),
    });
  }

  return (
      <ConfigProvider
          theme={{
            components: {
              Descriptions: {
                itemPaddingBottom: 0
              }
            },
          }}
      >
        <PageContainer
            title={false}
            header={{
              footer:
                  <Card>
                    <Space>
                      <Select
                          placeholder="Provider"
                          style={{width: 300}}
                          loading={providerLoading}
                          defaultActiveFirstOption
                          value={providerId}
                          onSelect={(value, option) => {
                            if (value === 'All') {
                              setProviderId(null);
                              history.replace(`/ai-service/models`);
                            } else {
                              setProviderId(value);
                              history.replace(`/ai-service/models?providerId=${value}`);
                            }
                          }}
                          options={
                            [{value: 'All', label: 'All'},
                              ...(providers ?? [])
                              .map((item) => ({label: item.name, value: item.providerId}))
                            ]
                          }
                      >
                      </Select>

                      <Select
                          placeholder="Status"
                          style={{width: 300}}
                          loading={providerLoading}
                          defaultActiveFirstOption
                          value={modelStatus}
                          onSelect={(value, option) => {
                            if (value === 'All') {
                              setModelStatus(undefined);
                            } else {
                              setModelStatus(value);
                            }
                          }}
                          options={
                            [{value: 'All', label: 'All'},
                              ...(allStatus ?? [])
                              .map((item) => ({label: item, value: item}))
                            ]
                          }
                      >
                      </Select>
                    </Space>
                  </Card>
            }}
        >
          <List<API.ModelResponse | PlaceholderType>
              rowKey={(item) => isNotPlaceholder(item) ? item.modelId : 'placeholder'}
              loading={loading}
              grid={{
                gutter: 16,
                xs: 1,
                sm: 2,
                md: 3,
                lg: 3,
                xl: 4,
                xxl: 4,
              }}
              dataSource={dataSource}
              renderItem={(item) => {
                if (isNotPlaceholder(item)) {
                  return (
                      <List.Item key={item.modelId}>
                        <Badge.Ribbon
                            text={item.status}
                            color={getStatusColor(item)}>
                          <Card
                              hoverable
                              className={styles.card}
                              actions={getActions(item)}
                          >
                            <Card.Meta
                                avatar={
                                  <Avatar size="large" style={{
                                    backgroundColor: hashColor(item.name),
                                    color: '#fff'
                                  }}>{item.name!.charAt(0)}</Avatar>
                                }
                                title={<a>{item.name}</a>}
                                description={
                                  <>
                                    <Descriptions layout="horizontal" column={1}>
                                      <Descriptions.Item
                                          label="Provider">{item.provider.name}</Descriptions.Item>
                                      <Descriptions.Item
                                          label="Context length">{item.maxOutputTokens}</Descriptions.Item>
                                      <Descriptions.Item
                                          label="Max output tokens">{item.maxOutputTokens}</Descriptions.Item>
                                      {/*<Descriptions.Item label="">
                                      <Paragraph
                                          className={styles.item}
                                          ellipsis={{
                                            rows: 3,
                                          }}
                                      >
                                        {item.description}
                                      </Paragraph>
                                    </Descriptions.Item>*/}
                                    </Descriptions>
                                  </>
                                }
                            />
                            <Flex gap="4px 0" wrap style={{marginTop: 16}}>
                              {item.features?.map(it => <Tag>{it}</Tag>)}
                            </Flex>
                          </Card>
                        </Badge.Ribbon>
                      </List.Item>
                  );
                }
                return (
                    <List.Item>
                      <Button
                          type="dashed"
                          className={styles.newButton}
                          onClick={() => {
                            setModelOpen(true);
                          }}
                      >
                        <PlusOutlined/>New
                      </Button>
                    </List.Item>
                );
              }}
          />
          <ModelModalForm
              open={modelOpen}
              mode={formMode}
              initialValues={selectedData}
              onSubmit={(values) => {
                if (formMode === 'create') {
                  return createModel({
                    providerId: values.providerId,
                    name: values.name,
                    enabled: values.enabled,
                    features: values.features
                  }).then(() => {
                    setModelOpen(false);
                    refresh();
                  });
                } else {
                  return patchModel({modelId: selectedData?.modelId!}, {
                    name: values.name,
                    enabled: values.enabled,
                    features: values.features
                  }).then(() => {
                    setModelOpen(false);
                    refresh();
                  });
                }
              }}
              onCancel={() => setModelOpen(false)}
          />

        </PageContainer>
      </ConfigProvider>
  );
};

export default AIService;
