// @ts-ignore
import {FormattedMessage, useIntl} from '@umijs/max';
import {Alert, DatePicker, Form, FormInstance, Input, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';
import {getModelFeatures} from '@/services/console/configController';
import {useRequest} from "ahooks";
import {getProviders} from "@/services/console/providerController";

const ModelModalForm: React.FC<Base.FormModelProps<API.ModelResponse, API.CreateModelRequest>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
  const [error, setError] = useState<Base.ProblemDetail>();
  const {data: features} = useRequest(() => getModelFeatures());

  const {data: providers, loading: providerLoading} = useRequest(() => getProviders().then(it => {
    if (it.length > 0) {
      formRef.setFieldValue('providerId', it[0].providerId)
    }
    return it;
  }));
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
            setError(undefined);
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
                ? {...props.initialValues, providerId: props.initialValues.provider?.providerId}
                : {
                  enabled: true,
                  sdkClientClass: 'openai'
                }
            }
            clearOnDestroy>

          <Form.Item name="providerId" label="Provider" rules={[
            {
              required: true,
            }
          ]}>
            <Select
                disabled={props.mode == 'edit'}
                loading={providerLoading}
                options={(providers ?? []).map(it => ({
                  value: it.providerId,
                  label: it.name,
                }))}/>
          </Form.Item>

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

          <Form.Item name="enabled" label="Enabled" rules={[
            {
              required: true
            },
          ]}>
            <Switch/>
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

export default ModelModalForm;
