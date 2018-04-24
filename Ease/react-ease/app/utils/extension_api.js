const extensionId = 'hnacegpfmpknpdjmhdmpkmedplfcmdmp';
const browser = window.msBrowser || window.chrome || window.browser;

const extension_api = {
  app_connection: ({app_id, active_tab, website}) => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'app_connection',
            data: {
              app_id: app_id,
              active_tab: active_tab,
              website: website
            }
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.response);
            else
              resolve(response.response);
          });
    });
  },
  test_website_connection: ({website_id, account_information}) => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'test_website_connection',
            data: {
              website_id: website_id,
              account_information: account_information
            }
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.response);
            else
              resolve(response.response);
          }
      )
    });
  },
  general_logout: () => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'generalLogout'
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.response);
            else
              resolve(response.response);
          }
      )
    });
  },
  set_homepage: ({state}) => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'setHomePage',
            data: state
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.response);
            else
              resolve(response.response);
          }
      )
    });
  },
  get_homepage: () => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'getHomePage'
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.response);
            else
              resolve(response.response);
          }
      )
    });
  },
  fillActiveTab: ({app_id}) => {
    browser.runtime.sendMessage(
        extensionId,
        {
          type: 'fillActiveTab',
          data: {
            app_id: app_id
          }
        }
    )
  },
  easeLogin: () => {
    !!browser && browser.runtime.sendMessage(
        extensionId,
        {
          type: 'easeLogin'
        }
    )
  },
  easeLogout: () => {
    !!browser && browser.runtime.sendMessage(
        extensionId,
        {
          type: 'easeLogout'
        }
    )
  },
  showPasswordUpdateAskHelperModal: ({appName, login}) => {
    !!browser && browser.runtime.sendMessage(
        extensionId,
        {
          type: 'showPasswordUpdateHelperModal',
          data: {
            appName: appName,
            login: login
          }
        }
    )
  }
};

export default extension_api;