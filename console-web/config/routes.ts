/**
 * @name umi 的路由配置
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */
export default [
  {
    path: '/auth',
    layout: false,
    routes: [
      {
        name: 'login',
        path: '/auth/login',
        component: './Auth/Login',
      },
    ],
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    icon: 'DashboardOutlined',
    component: './Welcome',
  },
  {
    path: '/playground',
    name: 'playground',
    icon: 'DashboardOutlined',
    component: './Playground',
  },
  {
    path: '/virtual-model',
    name: 'virtual-model',
    icon: 'crown',
    routes: [
      {
        path: '/virtual-model/models',
        name: 'model',
        component: './VirtualModel',
      },
      {
        path: '/virtual-model/keys',
        name: 'key',
        component: './VirtualKey',
      },
    ],
  },
  {
    path: '/ai-service',
    name: 'ai-service',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/ai-service/models',
        name: 'model',
        component: './Model',
      },
      {
        path: '/ai-service/keys',
        name: 'key',
        component: './Key',
      },
      {
        path: '/ai-service/providers',
        name: 'provider',
        component: './Provider',
      },
    ],
  },
  {
    path: '/settings',
    name: 'settings',
    icon: 'SettingOutlined',
    access: 'canAdmin',
    routes: [
      {
        path: '/settings/',
        name: 'sub-page',
        component: './Admin',
      },
    ],
  },
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/error/403',
    component: './403',
  },
  {
    path: '*',
    layout: false,
    component: './404',
  },
  /*{
    path: '/projects',
    name: 'project',
    icon: 'ProjectOutlined',
    routes: [
      {
        path: '/projects',
        redirect: '/projects/overview',
      },
      {
        path: '/projects/overview',
        name: 'overview',
        component: './Project/Overview',
      },
      {
        path: '/projects/:id/keys',
        name: 'key',
        hideInMenu: true,
        parentKeys: ['/projects/overview'],
        component: './Project/Key',
      },
      {
        path: '/projects/:id/members',
        name: 'member',
        hideInMenu: true,
        parentKeys: ['/projects/overview'],
        component: './Project/Member',
      },
      {
        path: '/projects/usage',
        name: 'usage',
        component: './Admin',
      },
    ],
  },
  {
    path: '/providers',
    name: 'provider',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/providers',
        redirect: '/providers/list',
      },
      {
        path: '/providers/list',
        name: 'list',
        component: './Model/List',
      },
      {
        path: '/providers/:id/models',
        name: 'model',
        hideInMenu: true,
        component: './Model/Group',
      },
    ],
  },
  {
    path: '/models',
    name: 'model',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {
        path: '/models',
        redirect: '/models/list',
      },
      {
        path: '/models/list',
        name: 'list',
        component: './Model/List',
      },
      {
        path: '/models/groups',
        name: 'group',
        component: './Model/Group',
      },
      {
        path: '/models/providers',
        name: 'provider',
        component: './Model/Provider',
      },
      {
        path: '/models/usage',
        name: 'usage',
        component: './Admin',
      },
    ],
  },
  {
    path: '/monitor',
    name: 'monitor',
    icon: 'MonitorOutlined',
    access: 'canAdmin',
    routes: [
      {
        path: '/monitor/logs',
        name: 'log',
        component: './Monitor/Log',
      },
      {
        path: '/monitor/healths',
        name: 'health',
        component: './Admin',
      },
    ],
  },*/
];
