// playwright.config.js
module.exports = {
  testDir: './tests',
  use: {
    baseURL: "http://localhost:8099",
    headless: false, // 看得见浏览器，方便调试
  },
};
