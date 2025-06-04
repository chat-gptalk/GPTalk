import {FormattedMessage, useIntl} from '@umijs/max';
import {DatePicker, Form, FormInstance, Input, Modal, Select, Switch, TreeSelect} from 'antd';
import React, {useRef, useState} from 'react';
import {getProviderTree, getProviderModels} from '@/services/console/providerController';
import {createModel} from '@/services/console/virtualModelController';
import dayjs from "dayjs";
import {useRequest} from "ahooks";


const ModalForm: React.FC<Base.FormModelProps<API.VirtualModelResponse>> = (props) => {
  const intl = useIntl();
  const [formRef] = Form.useForm();
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
            return await props.onSubmit(values);
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
            initialValues={props.initialValues
                ? {
                  ...props.initialValues,
                  name: props.initialValues.name?.substring(4),
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

export default ModalForm;
