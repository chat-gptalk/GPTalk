import {
  DeleteOutlined,
  EditOutlined,
  MoreOutlined,
  PlusOutlined
} from '@ant-design/icons';
import {PageContainer} from '@ant-design/pro-components';
import {useIntl, history} from '@umijs/max';
import {App, Avatar, Badge, Button, Card, Descriptions, Dropdown, List, MenuProps} from 'antd';
import {createProvider, getProviders, batchDeleteProvider} from '@/services/console/providerController';
import React, {useState} from 'react';
import {useRequest} from "ahooks";
import useStyles from './style.style';
import Paragraph from "antd/es/typography/Paragraph";
import ModalForm from "@/pages/Provider/components/ModalForm";
import {hashCode} from "@/utils/hash";

const Colors = ['#00BF6D', '#f56a00', '#7265e6', '#ffbf00', '#00a2ae'];
const AIService: React.FC = () => {
  const intl = useIntl();
  const {styles} = useStyles();

  const [modelOpen, setModelOpen] = useState(false);
  const [selectedData, setSelectedData] = useState<API.ProviderResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');

  const [selectedRowsState, setSelectedRows] = useState<API.ProviderResponse[]>([]);

  const {modal, message} = App.useApp();


  const {data: providers, loading, refresh} = useRequest((): Promise<API.ProviderResponse[]> => {
    return getProviders();
  });
  const nullData: Partial<API.ProviderResponse> = {};
  if (!providers) {
    return;
  }

  function getActions(item: Partial<API.ProviderResponse>) {
    const actions = [
      <Button type="link" href={`/ai-service/providers/${item.providerId}/models`}>Models</Button>,
      <Button type="link" href={`/ai-service/providers/${item.providerId}/keys`}>Keys</Button>,
    ];
    if (!item.system) {
      const items: MenuProps['items'] = [
        /*{
          key: '1',
          label: (
              <><EditOutlined/> Edit</>
          ),
        },*/
        {
          key: '2',
          label: (
              <a onClick={() => {
                modal.confirm({
                  title: 'Confirm',
                  content: 'Are you sure you want to delete this?',
                  onOk: () => batchDeleteProvider({
                    ids: [item.providerId!]
                  }).then(res => {
                    message.success('Deleted successfully');
                  }).catch(e => {
                    message.error(e.detail || 'Delete failed');
                  }).finally(() => {
                    setSelectedRows([]);
                    refresh();
                  }),
                });
              }}><DeleteOutlined style={{color: 'red'}}/> Delete</a>
          ),
        },
      ];
      actions.push(<Dropdown menu={{items}}>
        <Button type="text"><MoreOutlined/></Button>
      </Dropdown>);
    }
    return actions;
  }

  return (
      <PageContainer
          content="AI Service"
      >
        <List<Partial<API.ProviderResponse>>
            rowKey="providerId"
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
            dataSource={[nullData, ...providers]}
            renderItem={(item) => {
              if (item && item.providerId) {
                return (
                    <List.Item key={item.providerId}>
                      <Badge.Ribbon text={item.system ? 'System' : 'Custom'}
                                    color={item.system ? '#00BF6D' : '#1677ff'}>
                        <Card
                            hoverable
                            className={styles.card}
                            actions={getActions(item)}
                            onClick={() => {
                              history.push(`/ai-service/providers/${item.providerId}/models`);
                            }}
                        >
                          <Card.Meta
                              avatar={
                                item.extraConfig?.icon
                                    ? <img alt="" className={styles.cardAvatar} src={item.extraConfig?.icon ?? ''}/>
                                    : <Avatar size="large" style={{
                                      backgroundColor: Colors[hashCode(item.name!) % Colors.length],
                                      color: '#fff'
                                    }}>{item.name!.charAt(0)}</Avatar>
                              }
                              title={<a>{item.name}</a>}
                              description={
                                <>
                                  <Descriptions layout="horizontal" column={1}>
                                    <Descriptions.Item label="SDK Client">{item.sdkClientClass}</Descriptions.Item>
                                    <Descriptions.Item label="Model Count">{item.modelCount}</Descriptions.Item>
                                    <Descriptions.Item label="">
                                      <Paragraph
                                          className={styles.item}
                                          ellipsis={{
                                            rows: 3,
                                          }}
                                      >
                                        {item.description}
                                      </Paragraph>
                                    </Descriptions.Item>
                                  </Descriptions>
                                </>
                              }
                          />
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
        <ModalForm
            open={modelOpen}
            mode={formMode}
            initialValues={selectedData}
            onSubmit={(values) => {
              return createProvider(values)
              .then(it => {
                setModelOpen(false);
                refresh();
              });
            }}
            onCancel={() => setModelOpen(false)}
        />

      </PageContainer>
  );
};

export default AIService;
