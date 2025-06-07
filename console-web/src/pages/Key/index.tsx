import {ActionType, FooterToolbar, ProColumns} from '@ant-design/pro-components';
import {
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import {FormattedMessage, useIntl,} from '@umijs/max';
import {Typography, Badge, Button, App, Space, Select, Card} from 'antd';
import React, {useRef, useState} from 'react';
import {
  getProviderKeyValue,
  getProviderKeys,
  createProviderKey,
  patchProviderKey,
  batchDeleteProviderKey
} from "@/services/console/providerKeyController";
import {PlusOutlined} from "@ant-design/icons";
import ProviderKeyModalForm from "@/pages/Key/components/ProviderKeyModalForm";
import {useRequest} from "ahooks";
import {getProviders} from "@/services/console/providerController";
import {history, useSearchParams} from "@@/exports";

const {Text, Paragraph} = Typography;

const ProviderKeyList: React.FC = () => {
  const actionRef = useRef<ActionType>();

  const [modelOpen, setModelOpen] = useState(false);
  const [selectedData, setSelectedData] = useState<API.ProviderKeyResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');

  const [searchParams] = useSearchParams();

  const [providerId, setProviderId] = useState<string | null>(searchParams.get('providerId'));

  const [selectedRowsState, setSelectedRows] = useState<API.ProviderKeyResponse[]>([]);

  const {modal, message} = App.useApp();

  const {data: providers, loading: providerLoading} = useRequest(() => getProviders());

  if (!providers) {
    return null;
  }
  const intl = useIntl();
  const requestTableData = async ({providerId: pid}: any) => {
    const it = await getProviderKeys({
      providerId: pid,
    });
    return {
      data: it,
      total: it.length,
    };
  }

  const columns: ProColumns<API.ProviderKeyResponse>[] = [
    {
      title: 'ID',
      hideInSearch: true,
      dataIndex: 'providerKeyId',
    },
    {
      title: 'Name',
      dataIndex: 'name',
      hideInSearch: true,
      render: (_, record) => {
        return <Text copyable={{text: record.name}}>{record.name}</Text>;
      }
    },
    {
      title: 'Provider',
      dataIndex: 'provider',
      render: (_, record) => {
        return <>{record.provider.name}</>
      }
    },
    {
      title: 'Status',
      dataIndex: 'active',
      hideInSearch: true,
      render: (_, record) => {
        if (record.enabled) {
          return <Badge status="success" text="Active"/>;
        }
        return <Badge status="error" text="Inactive"/>;
      }
    },
    {
      title: 'Priority',
      dataIndex: 'priority',
      hideInSearch: true,
    },
    {
      title: 'Note',
      dataIndex: 'description',
      hideInSearch: true,
    },
    {
      title: 'Created at',
      dataIndex: 'createdAt',
      valueType: 'dateTime',
      hideInSearch: true,
    },
    {
      title: 'Updated at',
      dataIndex: 'updatedAt',
      valueType: 'dateTime',
      hideInSearch: true,
    },
    {
      title: '#',
      valueType: 'option',
      sorter: false,
      hideInForm: true,
      render: (_, record) => [
        <a
            key="view"
            onClick={async () => {
              getProviderKeyValue({providerKeyId: record.providerKeyId})
              .then((it) => {
                modal.info({
                  title: 'Provider Key',
                  content: <Paragraph copyable={{text: it.key}}>{it.key}</Paragraph>,
                })
              });
            }}
        >
          View Key
        </a>,
        <a
            key="edit"
            onClick={async () => {
              setSelectedData(record);
              setFormMode('edit');
              setModelOpen(true);
            }}
        >
          Edit
        </a>,
        <a
            key="delete"
            onClick={() => {
              doDelete([record.providerKeyId]);
            }}
        >
          Delete
        </a>
      ],
    },
  ];

  function doDelete(ids: string[]) {
    if (!ids || ids.length == 0) {
      return;
    }
    modal.confirm({
      title: 'Confirm',
      content: 'Are you sure you want to delete this?',
      onOk: () => batchDeleteProviderKey({
        ids
      }).then(res => {
        message.success('Deleted successfully');
      }).catch(e => {
        message.error(e.detail || 'Delete failed');
      }).finally(() => {
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
      }),
    });
  }

  return (
      <PageContainer title={false}
        header={{
          footer: <Card>
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
                      history.replace(`/ai-service/keys`);
                    } else {
                      setProviderId(value);
                      history.replace(`/ai-service/keys?providerId=${value}`);
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
            </Space>
          </Card>
        }}
      >
        <ProTable<API.ProviderKeyResponse, any>
            headerTitle='Keys'
            actionRef={actionRef}
            rowKey="providerKeyId"
            request={requestTableData}
            columns={columns}
            search={false}
            params={{providerId: providerId}}
            toolBarRender={() => [
              <Button
                  type="primary"
                  key="primary"
                  onClick={() => {
                    setModelOpen(true);
                  }}
              >
                <PlusOutlined/> <FormattedMessage id="common.btn.new" defaultMessage="New"/>
              </Button>,
            ]}
            rowSelection={{
              onChange: (_, selectedRows) => {
                setSelectedRows(selectedRows);
              },
            }}
        />
        {selectedRowsState?.length > 0 && (
            <FooterToolbar
                extra={
                  <div>
                    Chosen{' '}
                    <a style={{fontWeight: 600}}>{selectedRowsState.length}</a>{' '}
                    Items
                    &nbsp;&nbsp;
                  </div>
                }
            >
              <Button
                  color="danger"
                  variant="solid"
                  onClick={() => {
                    doDelete(selectedRowsState.map(it => it.providerKeyId!));
                  }}
              >
                Batch deletion
              </Button>
            </FooterToolbar>
        )}
        <ProviderKeyModalForm
            open={modelOpen}
            mode={formMode}
            initialValues={selectedData}
            onSubmit={(values) => {
              if (formMode === 'create') {
                return createProviderKey(values)
                .then(it => {
                  setModelOpen(false);
                  actionRef.current?.reload();
                });
              } else {
                return patchProviderKey({providerKeyId: selectedData?.providerKeyId!}, values)
                .then(it => {
                  setModelOpen(false);
                  actionRef.current?.reload();
                });
              }
            }}
            onCancel={() => setModelOpen(false)}
        />
      </PageContainer>
  );
};

export default ProviderKeyList;
