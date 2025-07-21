import {FormattedMessage, useIntl} from '@umijs/max';
import {Alert, DatePicker, Form, FormInstance, Input, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';
import {getProviderTree, getProviderModels} from '@/services/console/providerController';
import {useRequest} from "ahooks";


const VirtualModalForm: React.FC<Base.FormModelProps<API.VirtualModelResponse, API.CreateVirtualModelRequest>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
  const [error, setError] = useState<Base.ProblemDetail>();
  const {data: models, loading: modelLoading} = useRequest(async (): Promise<API.TreeNode[]> => {
    return getProviderTree();
  });
  return (
      <Modal
          width={640}
          styles={{
            body: {padding: '32px 40px 48px'}
          }}
          destroyOnHidden
          title={props.mode == 'create' ? 'New Model' : 'Edit Model'}
          open={props.open}
          onCancel={() => {
            props.onCancel();
          }}
          onOk={async () => {
            const values = await formRef.validateFields();
            setError(undefined);
            return await props.onSubmit({...values, name: `vm:${values.name}`})
            .catch(err => {
              setError(err)
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
                ? {
                  ...props.initialValues,
                  name: props.initialValues.name?.substring(3),
                  modelIds: props.initialValues.models?.map(model => model.model?.modelId),
                }
                : undefined
            }
            clearOnDestroy>
          <Form.Item name="name" label="Model Name" rules={[
            {
              required: true
            },
            {
              pattern: /^[a-z][a-z0-9-]{0,49}$/
            },
          ]}>
            <Input addonBefore="vm:"/>
          </Form.Item>

          {
            modelLoading
                ? null
                :
                <Form.Item
                    name="modelIds"
                    label="Models"
                    rules={[
                      {
                        required: true
                      },
                    ]}>
                  <TreeSelect
                      loading={modelLoading}
                      treeDefaultExpandAll
                      treeData={models ?? []}
                      multiple
                  />
                </Form.Item>
          }

          <Form.Item name="description" label="Description" rules={[
            {
              min: 2,
              max: 100,
            }
          ]}>
            <Input.TextArea/>
          </Form.Item>
        </Form>
      </Modal>
  );
};

export default VirtualModalForm;
