import {FormattedMessage, useIntl} from '@umijs/max';
import {Alert, DatePicker, Form, FormInstance, Input, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';
import {getModelFeatures} from '@/services/console/modelController';
import {useRequest} from "ahooks";

const ModalForm: React.FC<Base.FormModelProps<API.ModelResponse>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
  const [error, setError] = useState<Base.ProblemDetail>();
  const {data: features} = useRequest(() => getModelFeatures());
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

          <Form.Item name="features" label="Features" rules={[
            {
              required: true,
            }
          ]}>
            <Select
                mode="multiple"
                options={(features ?? []).map(it => ({
                  value: it,
                  label: it
                }))}/>
          </Form.Item>
        </Form>
      </Modal>
  );
};

export default ModalForm;
