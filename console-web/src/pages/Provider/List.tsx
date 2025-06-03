import {DeleteOutlined, EditOutlined, HeartTwoTone, SmileTwoTone} from '@ant-design/icons';
import {PageContainer} from '@ant-design/pro-components';
import {useIntl} from '@umijs/max';
import {Alert, Button, Card, Col, Row, Switch, Typography} from 'antd';
import React from 'react';

const providers: API.ProviderResponse[] = [];
const AIService: React.FC = () => {
  const intl = useIntl();
  return (
      <PageContainer
          content="AI Service"
      >
        <Row gutter={16}>
          {providers.map((p) => (
              <Col span={6} key={p.providerId}>
                <Card
                    title={p.name}
                    extra={<Switch checked={false} onChange={() => {
                    }}/>}
                    actions={[
                      <Button icon={<EditOutlined/>}/>,
                      <Button icon={<DeleteOutlined/>} danger/>,
                    ]}
                >
                  <p>类型：{p.name}</p>
                  <p>模型数：{p.modelCount}</p>
                </Card>
              </Col>
          ))}
        </Row>

      </PageContainer>
  );
};

export default AIService;
