import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'GPTalk',
          title: 'GPTalk',
          href: 'https://gptalk.chat',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/chat-gptalk/GPTalk',
          blankTarget: true,
        }
      ]}
    />
  );
};

export default Footer;
