import {ActionType, FooterToolbar, ProColumns} from '@ant-design/pro-components';
import {
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import {FormattedMessage, useIntl} from '@umijs/max';
import {Typography, Badge, Button, App,} from 'antd';
import React, {useRef, useState} from 'react';
import {
  getVirtualKeys,
  createVirtualKey,
  batchDeleteVirtualKey,
} from "@/services/console/virtualKeyController";
import {PlusOutlined} from "@ant-design/icons";
import VirtualKeyModalForm from "@/pages/VirtualKey/components/VirtualKeyModalForm";
import {downloadFile} from "@/utils/files";

const {Text, Paragraph} = Typography;

const VirtualKeyList: React.FC = () => {
  const actionRef = useRef<ActionType>();

  const [modelOpen, setModelOpen] = useState(false);
  const [selectedData, setSelectedData] = useState<API.VirtualKeyResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');


  const [selectedRowsState, setSelectedRows] = useState<API.VirtualKeyResponse[]>([]);

  const {modal, message} = App.useApp();
  const intl = useIntl();
  const requestTableData = async () => {
    const it = await getVirtualKeys();
    return {
      data: it,
      total: it.length,
    };
  }

  const columns: ProColumns<API.VirtualKeyResponse>[] = [
    {
      title: 'ID',
      hideInSearch: true,
      dataIndex: 'virtualKeyId',
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
      title: 'Status',
      dataIndex: 'active',
      hideInSearch: true,
      render: (_, record) => {
        if (record.status === 1) {
          return <Badge status="success" text="Active"/>;
        }
        return <Badge status="error" text="Inactive"/>;
      }
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
      title: 'Last used at',
      dataIndex: 'lastUsedAt',
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
            key="delete"
            onClick={() => {
              doDelete([record.virtualKeyId]);
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
      onOk: () => batchDeleteVirtualKey({
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
      <PageContainer title={false}>
        <ProTable<API.VirtualKeyResponse, any>
            headerTitle='Keys'
            actionRef={actionRef}
            rowKey="virtualKeyId"
            request={requestTableData}
            columns={columns}
            search={false}
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
                    doDelete(selectedRowsState.map(it => it.virtualKeyId!));
                  }}
              >
                Batch deletion
              </Button>
            </FooterToolbar>
        )}
        <VirtualKeyModalForm
            open={modelOpen}
            mode={formMode}
            initialValues={selectedData}
            onSubmit={(values) => {
              return createVirtualKey(values)
              .then(it => {
                setModelOpen(false);
                actionRef.current?.reload();
                modal.info({
                  title: 'Saved successfully',
                  okText: 'Save',
                  onOk: () => {
                    downloadFile(it.key, `secret-key_${new Date().getTime()}.txt`);
                  },
                  content: <>
                    <Paragraph><Text type="danger">Please save this secret key somewhere safe and accessible. For
                      security reasons, <b>you will not be able to view it again</b>. If you lose this secret key, you
                      will need to generate a new one.</Text></Paragraph>
                    <Paragraph copyable={{text: it.key}}>{it.key}</Paragraph>
                  </>
                });
              });
            }}
            onCancel={() => setModelOpen(false)}
        />
      </PageContainer>
  );
};

export default VirtualKeyList;
