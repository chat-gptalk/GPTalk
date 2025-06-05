import {ActionType, FooterToolbar, ProColumns} from '@ant-design/pro-components';
import {
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import {FormattedMessage, useIntl,} from '@umijs/max';
import {Typography, Badge, Button, App} from 'antd';
import React, {useRef, useState} from 'react';
import {getModels, createModel, batchDelete} from "@/services/console/virtualModelController";
import {PlusOutlined} from "@ant-design/icons";
import ModalForm from "@/pages/Model/components/ModalForm";

const {Text} = Typography;

const ModelList: React.FC = () => {
  const actionRef = useRef<ActionType>();

  const [modelOpen, setModelOpen] = useState(false);
  const [selectedData, setSelectedData] = useState<API.VirtualModelResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');

  const [selectedRowsState, setSelectedRows] = useState<API.VirtualModelResponse[]>([]);

  const {modal, message} = App.useApp();

  const intl = useIntl();
  const requestTableData = async () => {
    const it = await getModels();
    return {
      data: it,
      total: it.length,
    };
  }


  const columns: ProColumns<API.VirtualModelResponse>[] = [
    {
      title: 'ID',
      hideInSearch: true,
      dataIndex: 'virtualModelId',
    },
    {
      title: 'Name',
      dataIndex: 'name',
      hideInSearch: true,
      render: (_, record) => {
        return <Text copyable={{text: record.name}}>{record.name}</Text>;
      }
    },
    /*{
      title: 'Provider',
      dataIndex: 'provider.name',
      hideInSearch: true,
      render: (_, record) => {
        return <>{record.provider?.alias}</>
      }
    },*/
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
    /*{
      title: 'Health Status',
      dataIndex: 'health_status',
      hideInSearch: true,
      render: (_, record) => {
        if (record.healthStatus === 'HEALTHY') {
          return <Badge status="success" text="HEALTHY"/>;
        }
        return <Badge status="error" text="UNHEALTHY"/>;
      }
    },
    {
      title: 'Priority',
      dataIndex: 'priority',
      hideInSearch: true,
    },*/
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
        /*<a
            key="edit"
            onClick={async () => {
              setSelectedData(record);
              setFormMode('create');
              setModelOpen(true);
            }}
        >
          Edit
        </a>,*/
        <a
            key="delete"
            onClick={() => {
              doDelete([record.virtualModelId]);
            }}
        >
          Delete
        </a>
      ],
    },
  ];

  function doDelete(ids: string[]) {
    if (!selectedRowsState || selectedRowsState.length == 0) {
      return;
    }
    modal.confirm({
      title: 'Confirm',
      content: 'Are you sure you want to delete this?',
      onOk: () => batchDelete({
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
        <ProTable<API.VirtualModelResponse, any>
            headerTitle='Models'
            actionRef={actionRef}
            rowKey="virtualModelId"
            search={false}
            request={requestTableData}
            columns={columns}
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
                    doDelete(selectedRowsState.map(it => it.virtualModelId!));
                  }}
              >
                Batch deletion
              </Button>
            </FooterToolbar>
        )}
        <ModalForm
            open={modelOpen}
            mode={formMode}
            initialValues={selectedData}
            onSubmit={(values) => {
              return createModel(values)
              .then(it => {
                setModelOpen(false);
                actionRef.current?.reload();
              });
            }}
            onCancel={() => setModelOpen(false)}
        />
      </PageContainer>
  );
};

export default ModelList;
