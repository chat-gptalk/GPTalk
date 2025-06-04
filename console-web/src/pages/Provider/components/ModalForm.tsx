import {FormattedMessage, useIntl} from '@umijs/max';
import {Alert, DatePicker, Form, FormInstance, Input, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';


const ModalForm: React.FC<Base.FormModelProps<API.ProviderResponse>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
  const [error, setError] = useState<Base.ProblemDetail>();
  return (
      <Modal
          width={640}
          styles={{
            body: {padding: '32px 40px 48px'}
          }}
          destroyOnHidden
          title={props.mode == 'create' ? 'New' : 'Edit'}
          open={props.open}
          onCancel={() => {
            props.onCancel();
          }}
          onOk={async () => {
            const values = await formRef.validateFields();
            return await props.onSubmit(values).catch(err => {
              setError(err);
            });
          }}
      >
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
        <Form
            form={formRef}
            labelCol={{
              span: 6
            }}
            wrapperCol={{
              span: 18
            }}
            initialValues={props.initialValues
                ? props.initialValues
                : {
                  sdkClientClass: 'openai'
                }
            }
            clearOnDestroy>
          <Form.Item name="name" label="Name" rules={[
            {
              required: true
            },
            {
              min: 2,
              max: 50,
            },
          ]}>
            <Input/>
          </Form.Item>

          <Form.Item name="baseUrl" label="Base Url" rules={[
            {
              required: true
            },
            {
              pattern: /^(https?:\/\/)?([a-zA-Z0-9.-]+)(:\d+)?(\/[^\s]*)?$/
            },
          ]}>
            <Input/>
          </Form.Item>

          <Form.Item name="description" label="Description" rules={[
            {
              max: 200
            },
          ]}>
            <Input.TextArea/>
          </Form.Item>

          <Form.Item name="sdkClientClass" label="SDK Client" rules={[
            {
              required: true,
            }
          ]}>
            <Select>
              <Select.Option value="openai">Open AI</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
  );
};

export default ModalForm;
