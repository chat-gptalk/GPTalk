import {PlusOutlined} from '@ant-design/icons';
import {PageContainer} from '@ant-design/pro-components';
import {useIntl, useParams} from '@umijs/max';
import {App, Avatar, Badge, Button, Card, Descriptions, List, Switch, Tag} from 'antd';
import {
  getProviderModels,
  createProviderModel,
  batchDeleteProviderModel,
  getProvider
} from '@/services/console/providerController';
import React, {useState} from 'react';
import {useRequest} from "ahooks";
import useStyles from './style.style';
import ModalForm from "@/pages/Provider/Model/components/ModalForm";
import {hashCode} from "@/utils/hash";

const Colors = ['#00BF6D', '#f56a00', '#7265e6', '#ffbf00', '#00a2ae'];
const AIService: React.FC = () => {
  const intl = useIntl();
  const {styles} = useStyles();
  const {providerId} = useParams();

  const [modelOpen, setModelOpen] = useState(false);
  const [selectedData, setSelectedData] = useState<API.ProviderResponse>();
  const [formMode, setFormMode] = useState<Base.FormMode>('create');

  const [selectedRowsState, setSelectedRows] = useState<API.ProviderResponse[]>([]);

  const {modal, message} = App.useApp();
  const {data: provider} = useRequest(() => getProvider({providerId}));

  const {data: models, loading, refresh} = useRequest((): Promise<API.ModelResponse[]> => {
    return getProviderModels({providerId: providerId});
  });
  const nullData: Partial<API.ModelResponse> = {};
  if (!models) {
    return;
  }

  function getActions(item: Partial<API.ModelResponse>) {
    return [
      <Switch value={item.enabled}/>,
      <Button type="text"></Button>
    ];
  }

  let dataSource = [];
  console.log(provider);
  if (provider && provider.system) {
    dataSource = models;
  } else {
    dataSource = [nullData, ...models];
  }
  return (
      <PageContainer
          content="AI Service"
      >
        <List<Partial<API.ModelResponse>>
            rowKey="modelId"
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
              if (item && item.modelId) {
                return (
                    <List.Item key={item.modelId}>
                      <Badge.Ribbon
                          text={item.status}
                          color={item.status === 'HEALTHY' ? '#00BF6D' : 'red'}>
                        <Card
                            hoverable
                            className={styles.card}
                            actions={getActions(item)}
                        >
                          <Card.Meta
                              avatar={
                                <Avatar size="large" style={{
                                  backgroundColor: Colors[hashCode(item.name!) % Colors.length],
                                  color: '#fff'
                                }}>{item.name!.charAt(0)}</Avatar>
                              }
                              title={<a>{item.name}</a>}
                              description={
                                <>
                                  <Descriptions layout="horizontal" column={1}>
                                    <Descriptions.Item label="Context length">{item.maxOutputTokens}</Descriptions.Item>
                                    <Descriptions.Item
                                        label="Max output tokens">{item.maxOutputTokens}</Descriptions.Item>
                                    <Descriptions.Item
                                        label="">{item.features?.map(it => <Tag>{it}</Tag>)}</Descriptions.Item>
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
              return createProviderModel({providerId}, {
                name: values.name,
                features: values.features
              }).then(() => {
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
