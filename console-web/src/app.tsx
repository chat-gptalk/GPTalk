import {AvatarDropdown, AvatarName, Footer, Question, SelectLang} from '@/components';
import {LinkOutlined, UserOutlined} from '@ant-design/icons';
import type {Settings as LayoutSettings} from '@ant-design/pro-components';
import {SettingDrawer} from '@ant-design/pro-components';
import type {RunTimeLayoutConfig} from '@umijs/max';
import { App } from 'antd';
import {history, Link} from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import {errorConfig} from './requestErrorConfig';
import {me as queryCurrentUser} from '@/services/console/userController';

import React from 'react';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/auth/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.ConsoleAuthenticatedUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.ConsoleAuthenticatedUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      return await queryCurrentUser({
        skipErrorHandler: true,
      });
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  const {location} = history;
  if (location.pathname !== loginPath) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }
  return {
    fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  return {
    actionsRender: () => [<Question key="doc"/>, <SelectLang key="SelectLang"/>],
    avatarProps: {
      title: <AvatarName/>,
      icon: <UserOutlined />,
      style: { backgroundColor: '#87d068' },
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
   /*waterMarkProps: {
      content: initialState?.currentUser?.name,
    },*/
    footerRender: () => <Footer/>,
    onPageChange: () => {
      const {location} = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        history.push(loginPath);
      }
    },
    bgLayoutImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
        ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined/>
            <span>OpenAPI 文档</span>
          </Link>,
        ]
        : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
          <App>
            {children}
            {isDev && (
                <SettingDrawer
                    disableUrlParams
                    enableDarkTheme
                    settings={initialState?.settings}
                    onSettingChange={(settings) => {
                      setInitialState((preInitialState) => ({
                        ...preInitialState,
                        settings,
                      }));
                    }}
                />
            )}
          </App>
      );
    },
    ...initialState?.settings,
  };
};


export const request = {
  ...errorConfig,
};
