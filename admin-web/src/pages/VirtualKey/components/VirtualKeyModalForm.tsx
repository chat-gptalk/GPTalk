import {FormattedMessage, useIntl} from '@umijs/max';
import {Alert, DatePicker, Form, FormInstance, Input, InputNumber, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';
import {getProviders} from '@/services/console/providerController';
import {useRequest} from "ahooks";

const VirtualKeyModalForm: React.FC<Base.FormModelProps<API.ProviderKeyResponse, API.CreateProviderKeyRequest>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
  const [error, setError] = useState<Base.ProblemDetail>();
  const {data: providers, loading: providersLoading} = useRequest(async (): Promise<API.ProviderResponse[]> => {
    return getProviders();
  });
  return (
      <Modal
          width={640}
          styles={{
            body: {padding: '32px 40px 48px'}
          }}
          destroyOnHidden
          title={'New Key'}
          open={props.open}
          onCancel={() => {
            props.onCancel();
          }}
          onOk={async () => {
            const values = await formRef.validateFields();
            setError(undefined);
            return await props.onSubmit(values).catch((e) => setError(e));
          }}
      >
        <Form
            form={formRef}
            labelCol={{
              span: 6
            }}
            wrapperCol={{
              span: 18
            }}
            clearOnDestroy>
          {
              error && <Alert
                  style={{
                    marginBottom: 24,
                  }}
                  message={error.detail}
                  type="error"
                  showIcon
              />
          }
          <Form.Item name="name" label="Name" rules={[
            {
              required: true
            },
            {
              min: 2,
              max: 50
            }
          ]}>
            <Input/>
          </Form.Item>
        </Form>
      </Modal>
  );
};

export default VirtualKeyModalForm;
