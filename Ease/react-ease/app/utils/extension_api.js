const extensionId = 'hnacegpfmpknpdjmhdmpkmedplfcmdmp';
const browser = window.msBrowser || window.chrome || window.browser;

const extension_api = {
  app_connection: ({app_id, active_tab}) => {
    return new Promise((resolve, reject) => {
      browser.runtime.sendMessage(
          extensionId,
          {
            type: 'app_connection',
            data: {
              app_id: app_id,
              active_tab: active_tab
            }
          },
          {},
          (response) => {
            if (!response && !!browser.runtime.lastError)
              reject(browser.runtime.lastError.message);
            else if (response.error)
              reject(response.data);
            else
              resolve(response.data);
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
              reject(response.data);
            else
              resolve(response.data);
          }
      )
    });
  }
};

export default extension_api;